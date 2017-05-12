package service;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

public class HttpService {

	private static Map<Integer, Book> books = new HashMap<Integer, Book>();

	private static Gson gson = new Gson();

	public static void main(String[] args) {
		System.out.println("Starting HTTP service...");

		Random random = new Random();

		System.out.println("Initializing map..");
		books.put(random.nextInt(1000), new Book("fine", "even more fine"));

		get("/book", (req, res) -> {
			String lookingForAuthor = req.queryParams("author");
			System.out.println("looking for author: " + lookingForAuthor);

			for (Book b : books.values()) {
				System.out.println("Currently checking book: " + b);
				if (b.getAuthors()[0].equals(lookingForAuthor)) {
					// create JSON representation
					String bookString = gson.toJson(b);
					return bookString;
				}
			}
			return "no book by this author";
		});

		get("/hello", (req, res) -> {

			Book book = new Book("Bloch", "Effective Java", 300);

			// create JSON representation of the `book` object
			String bookString = gson.toJson(book);
			System.out.println("Created the following JSON:");
			System.out.println(bookString);

			return bookString;
		});

		post("/addbook", (request, response) -> {
			// retrieve information from request
			String json = request.queryParams("book");
			System.out.println("book JSON: " + json);

			// create instance of model class
			Book newBook  = gson.fromJson(json, Book.class);

			// create new ID
			int ID = random.nextInt(1000);

			// store new book object (in memory)
			// note: ID is converted to Integer using auto-boxing
			books.put(ID, newBook);
			System.out.println("We just added the new book " + newBook + " under ID " + ID);

			return Integer.toString(ID);
		});
	}

}
