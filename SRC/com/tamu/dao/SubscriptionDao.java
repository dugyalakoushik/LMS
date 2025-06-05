package com.tamu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import com.tamu.dto.MyBookDto;
import com.tamu.entity.Subscription;

public class SubscriptionDao {

	private MongoClient mongoClient = null;
	private static String conStr;
	Gson gson = new Gson();

	public void connect(String str) {

		Properties properties = Config.loadProperties();

		String mongoPassword = properties.getProperty("mongo.password");
		conStr = "mongodb+srv://admin:" + mongoPassword + "@csce606.xubngdw.mongodb.net/?retryWrites=true&w=majority";
		System.out.println(conStr);

		if (mongoClient != null)
			mongoClient.close();
		mongoClient = new MongoClient(new MongoClientURI(str));
	}

	public void insertSubscription(Subscription subscription) {
		try {
			this.connect(conStr);
			MongoDatabase database = mongoClient.getDatabase("csce606"); // Replace with your database name
			MongoCollection<Document> subscriptionsCollection = database.getCollection("subscriptions");

			subscriptionsCollection.insertOne(subscription.toDocument());
		} catch (Exception e) {
			System.out.println("Error!!!");
		}

	}

	public List<MyBookDto> getSubscription(int userId) {
		try {
			this.connect(conStr);
			MongoDatabase database = mongoClient.getDatabase("csce606"); // Replace with your database name
			MongoCollection<Document> subscriptionsCollection = database.getCollection("subscriptions");
			
			int count =  (int) subscriptionsCollection
					.countDocuments(Filters.and(Filters.eq("user.userId", userId), Filters.eq("isReturned", 0)));
			
			FindIterable<Document> result = subscriptionsCollection
					.find(Filters.and(Filters.eq("user.userId", userId), Filters.eq("isReturned", 0))) .projection(Projections.excludeId());;
					

			List<MyBookDto> subscriptions = new ArrayList<>();
			for (Document doc : result) {
				 Subscription subscription = gson.fromJson(doc.toJson(), Subscription.class);
				    subscriptions.add(mapDocumentToMyBookDto(subscription));
			}

			return subscriptions;
		} catch (Exception e) {
			System.out.println("Error!!");
		}
		return null;
	}

	private MyBookDto mapDocumentToMyBookDto(Subscription subscription) {
		MyBookDto myBookDto = new MyBookDto();
		myBookDto.setSubscriptionId((int)subscription.getSubscriptionId());
		myBookDto.setBookId(subscription.getBook().getBookId());
		myBookDto.setBookName(subscription.getBook().getBookName());
		myBookDto.setBookDescription(subscription.getBook().getBookDescription());
		myBookDto.setAuthorName(subscription.getBook().getAuthorName());
		myBookDto.setBookPrice(subscription.getBook().getBookPrice());
		myBookDto.setQuantity(subscription.getBook().getQuantity());
		myBookDto.setGenre(subscription.getBook().getGenre());
		myBookDto.setImageURL("");
		myBookDto.setOrderDate(subscription.getOrderDate().toString());
		myBookDto.setDueDate(subscription.getDueDate().toString());
		return myBookDto;
	}

	public int getSubscriptionsByUserId(int userId) {
		try {
			this.connect(conStr);
			MongoDatabase database = mongoClient.getDatabase("csce606"); // Replace with your database name
			MongoCollection<Document> subscriptionsCollection = database.getCollection("subscriptions");

			return (int) subscriptionsCollection
					.countDocuments(Filters.and(Filters.eq("user.userId", userId), Filters.eq("isReturned", 0)));
		} catch (Exception e) {
			System.out.println("Error");
		}
		return 0;
	}
	
	public boolean unsubscribe (int subscriptionId) {
		this.connect(conStr);
		MongoDatabase database = mongoClient.getDatabase("csce606"); // Replace with your database name
		MongoCollection<Document> subscriptionsCollection = database.getCollection("subscriptions");
		
		 Document filter = new Document("subscriptionId", subscriptionId);

         Document update = new Document("$set", new Document("isReturned", 1));

         UpdateResult result = subscriptionsCollection.updateOne(filter, update);
         
         return  result.getMatchedCount() == 1 && result.getModifiedCount() == 1;
				
	}

}
