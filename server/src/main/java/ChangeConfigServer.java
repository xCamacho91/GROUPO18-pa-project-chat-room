import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ChangeConfigServer extends Thread {

    private Semaphore numberOfConcurrentRequests;

    private ArrayList<String> filterWords;

    public ChangeConfigServer(Semaphore numberOfConcurrentRequests, ArrayList<String> filterWords) {
        this.numberOfConcurrentRequests = numberOfConcurrentRequests;
        this.filterWords = filterWords;
    }


    @Override
    public void run ( ) {
        try {
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);

                    if (inputLine.startsWith("/users")) {
                        String[] substrings = inputLine.split(" ");
                        try {
                            chageNumberOfConcurrentRequests(substrings[1]);
                        } catch (ArrayIndexOutOfBoundsException e1) {
                            System.out.println("You must enter a number after /users");
                        }
                    } else if (inputLine.startsWith("/filter")) {
                        String[] substrings = inputLine.split(" ");
                        try {
                            handleFilter(substrings[1]);
                        } catch (ArrayIndexOutOfBoundsException e2) {
                            System.out.println("You must enter a string after /filter");
                        }
                    }
                }
                in.close();
                // Parse the response to extract the prompt
                String prompt = response.toString();
                // Do something with the prompt
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filter - string to add or remove from the list of filter words
     */
    private void handleFilter(String filter) {
        if (filterWords.contains(filter)) {
            filterWords.remove(filter);
            System.out.println(filter + " has remove from the list of filter words");
        } else {
            filterWords.add(filter);
            System.out.println(filter + " has been added to the list of filter words");
        }
    }

    /**
     * @param quantity - number to change the number of concurrent requests
     */
    private void chageNumberOfConcurrentRequests(String quantity) {
        try {
            int num = Integer.parseInt(quantity);
            if (num > numberOfConcurrentRequests.availablePermits()) {
                int permitsToRelease = num - numberOfConcurrentRequests.availablePermits();
                numberOfConcurrentRequests.release(permitsToRelease); // Releases additional permits if needed
            } else if (num < numberOfConcurrentRequests.availablePermits()) {
                int permitsToAcquire = numberOfConcurrentRequests.availablePermits() - num;
                numberOfConcurrentRequests.acquire(permitsToAcquire); // Acquires additional permits if needed
            }
            System.out.println("Number of concurrent requests, has been change successfully to " + num);
        } catch (NumberFormatException e) {
            System.out.println(quantity + " is not a valid prompt, after /users you must enter a number");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
