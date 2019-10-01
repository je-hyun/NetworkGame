import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(59898)) { // Create a listener
            System.out.println("The capitalization server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(20); // Make 20-thread pool
            while (true) {
							  // Waits for client to connect, then run GameThread.run() with
								// the socket to the client that connected.
                pool.execute(new GameThread(listener.accept()));
            }
        }
    }

    private static class GameThread implements Runnable {
        private Socket socket;
				private Place currentPlace;

        GameThread(Socket socket) {
					this.socket = socket; // This socket is found by listener.accept()
					// Initialize the game's map
					this.currentPlace = new Place("Start", "There is a blue and red door. Which one to pick?");
					Place blueDoor = new Place("Blue Door", "You walk through the blue door. There doesn't seem to be anything of interest here yet.");
					Place redDoor = new Place("Red Door", "You walk through the red door. There is a hole in the ground, so deep you can't see the bottom.");
					Place hole = new Place("Hole", "You fell in the hole. There doesn't seem to be a way up.");
					Place tunnel = new Place("Tunnel", "The tunnel leads to a dead end.");
					Place.connect(this.currentPlace, redDoor);
					Place.connect(this.currentPlace, blueDoor);
					Place.connectOneWay(redDoor, hole);
					Place.connect(hole, tunnel);
        }

				public String response(String clientText) {
					ArrayList<Place> currentNeighbors = this.currentPlace.getNeighbors();
					for (Place pl : currentNeighbors) {
						if (clientText.equals(pl.getName())) {
							this.currentPlace = pl;
						}
					}
					return this.currentPlace.toString();
				}

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
							// THIS block is the juicy stuff!!
                Scanner in = new Scanner(socket.getInputStream()); // Scanner input from client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // PrintWriter output to client
                while (in.hasNextLine()) {
									  // Respond to next client input line
                    out.println(response(in.nextLine()));
                }


					  // Rest of the stuff is boring exception handling, socket closing
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }
    }
}
