package com.example;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class Document implements Comparable<Document>{
    String name;
    String description;
    String createdBy;
    String lastModifiedBy;
    Long sizeInBytes;
    Long createdTime;
    Long modifiedTime;


    /**
     * Prints a report of the list of documents in the following format:
     *
     * Group by document.createdBy
     * Sort the groups using document.createdBy ascending, case insensitive
     *      Sort each sub list of documents by document.createdTime ascending
     * Format the output of document.size to be a more friendly format. Ex.  50 mb, 900 k, 342 bytes, etc...
     * Format the dates using the format: yyyy-MM-dd
     * Format the output of document.description such that
     *  - no more than the first 25 characters of the description are displayed
     *  - don't truncate any words unless the first word is longer than 25 characters
     *  - display "..." at the end of the description to indicate that it has been truncated 
     *  (these three characters do not count as part of the 25 character limit)
     *
     * Example:
     * Andy Andrews
     *      "Bobby Timmons Biography","An exhaustive look at the ...",233 mb,2013-05-09,2013-05-14
     *      "Apple Sauce","Study of apple sauces.”,87 gb,2013-05-10,2013-05-10
     *      "Zed","All matters, A to Zed”,924 k,2013-05-12,2013-05-12
     * Janet Smith
     *      "Xray","How the Xray shows your ...",48 mb,2010-10-22,2010-12-02
     *      "Computers","Inventory list of ...",423 bytes,2013-03-01,2013-02-17
     *
     *
     * @param documents not null
     */
    public void printDocumentsReport(List<Document> documents) {
    	//Collections.sort(documents);
    	/*for(Document doc : documents) {
    		this.printDocumentInfo(doc);
    	}*/
    	Map<Object, List<Document>> result = documents.stream().collect(Collectors.groupingBy(d -> d.createdBy));
    	result = new TreeMap<Object, List<Document>>(result);
    	
    	for(Map.Entry<Object, List<Document>> entry : result.entrySet()) {
    		System.out.println(entry.getKey());
    		List<Document> list = entry.getValue();
    		Collections.sort(list, new CreatedTimeComparator());
    		Iterator<Document> iter = list.iterator();
    		while(iter.hasNext()) {
    			this.printDocumentInfo(iter.next());
    		}
    	}

    }
    
    private void printDocumentInfo(Document doc) {
    	//System.out.println(doc.createdBy);
    	System.out.println("     " + doc.name + "," + doc.formatDescription(doc.description) + "," + doc.formatSize(doc.sizeInBytes) + "," + doc.formatDate(doc.createdTime) + "," + doc.formatDate(doc.modifiedTime));
    }
    
    private String formatSize(Long sizeInBytes) {
    	if (sizeInBytes < 1024) 
    		return sizeInBytes + " B";
    	int z = (63 - Long.numberOfLeadingZeros(sizeInBytes)) / 10;
    	return String.format("%.1f %sB", (double)sizeInBytes / (1L << (z*10)), " KMGTPE".charAt(z));  
    }
    
    private String formatDate(Long dateTime) {
    	LocalDate date = LocalDate.ofEpochDay(dateTime);
        return date.toString();   
    }
    
    private String formatDescription(String description) {
    	if(description.length() > 25) {
    		return description.substring(0, 25) + "...";
    	}
    	return description;
 
    }
    
	public Document(String name, String description, String createdBy, String lastModifiedBy, Long sizeInBytes,
			Long createdTime, Long modifiedTime) {
		super();
		this.name = name;
		this.description = description;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
		this.sizeInBytes = sizeInBytes;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
	}
	
	class CreatedByComparator implements Comparator<Document>{
		
		public int compare(Document doc1, Document doc2) {
			return doc1.createdBy.compareToIgnoreCase(doc2.createdBy);
		}
	}
	
	class CreatedTimeComparator implements Comparator<Document>{
		
		public int compare(Document doc1, Document doc2) {
			return doc1.createdTime.compareTo(doc2.createdTime);
		}
	}

    public Document() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
    	
    	List<Document> documents = Arrays.asList(
                new Document("Bobby Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "Zebra Zoo","Andy Andrews",(long)388787, LocalDate.of(2015,2,20).toEpochDay(), (long)33434),
                new Document("Bobby Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "Mindy Man","Andy Andrews",(long)388789, LocalDate.of(2016,6,21).toEpochDay(), (long)33434),
                new Document("Bobby Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "George Geo","Andy Andrews",(long)4444, LocalDate.of(2011,1,5).toEpochDay(), (long)33434),
                new Document("Yolanda Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "Alley Cat","Andy Andrews",(long)388787, LocalDate.of(2017,2,9).toEpochDay(), (long)33434),
                new Document("Bobby Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "Zebra Zoo","Andy Andrews",(long)33366666, LocalDate.of(2018,8,3).toEpochDay(), (long)33434),
                new Document("Hero Timmons Biography", "An exhaustive look", "Bobby Timmons","Andy Andrews",(long)388787, LocalDate.of(2018,2,20).toEpochDay(), (long)33434),
                new Document("Bobby Timmons Biography", "An exhaustive look at the long and lovely life of Bobby.", "Mindy Man","Andy Andrews",(long)382787, LocalDate.of(2019,12,20).toEpochDay(), (long)33434),
                new Document("Janet Smith Biography", "An exhaustive look at the long and lovely life of Bobby.", "Alley Cat","Andy Andrews",(long)38078, LocalDate.of(2020,6,20).toEpochDay(), (long)33434)
                );
    	
    	Document mydoc= new Document();

    	mydoc.printDocumentsReport(documents);

    }

	@Override
	public int compareTo(Document o) {
		if(this.createdBy.compareToIgnoreCase(o.createdBy) == 0) {
			return LocalDate.ofEpochDay(this.createdTime).compareTo(LocalDate.ofEpochDay(o.createdTime));
		}
		return this.createdBy.compareToIgnoreCase(o.createdBy);
	}



	
}
