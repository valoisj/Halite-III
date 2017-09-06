package hlt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class Networking {

    private static final char UNDOCK_KEY = 'u';
    private static final char DOCK_KEY = 'd';
    private static final char THRUST_KEY = 't';

    public static void sendMoves(ArrayList<Move> moves) {
        StringBuilder moveString = new StringBuilder();

        for (Move move : moves) {
            switch (move.getType()) {
                case Noop:
                    continue;
                case Undock:
                    moveString.append(UNDOCK_KEY)
                            .append(" ")
                            .append(move.getShip().getEntityId().getId())
                            .append(" ");
                    break;
                case Dock:
                    moveString.append(DOCK_KEY)
                            .append(" ")
                            .append(move.getShip().getEntityId().getId())
                            .append(" ")
                            .append(((DockMove) move).getDestination().getId())
                            .append(" ");
                    break;
                case Thrust:
                    moveString.append(THRUST_KEY)
                            .append(" ")
                            .append(move.getShip().getEntityId().getId())
                            .append(" ")
                            .append(((ThrustMove) move).getThrust())
                            .append(" ")
                            .append(((ThrustMove) move).getAngle())
                            .append(" ");
                    break;
            }
        }
        System.out.println(moveString);
    }

    private static String readLine() {
        try {
            StringBuilder builder = new StringBuilder();
            int buffer;

            for ( ; (buffer = System.in.read()) >= 0; ) {
                if (buffer == '\n') {
                    break;
                }
                if (buffer == '\r') {
                    // Ignore carriage return if on windows for manual testing.
                    continue;
                }
                builder = builder.append((char)buffer);
            }
            return builder.toString();
        }
        catch(Exception e) {
            System.exit(1);
            return null;
        }
    }

    public static LinkedList<String> readAndSplitLine() {
        return new LinkedList<>(Arrays.asList(readLine().trim().split(" ")));
    }
    
    public GameMap initialize(String botName) {
        final short myId = Short.parseShort(readLine());
        try {
            DebugLog.initialize(new FileWriter(String.format("%d - %s.log", myId, botName)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        final LinkedList<String> inputStringMapSize = readAndSplitLine();
        final short width = Short.parseShort(inputStringMapSize.pop());
        final short height = Short.parseShort(inputStringMapSize.pop());
        GameMap gameMap = new GameMap(width, height, myId);

        // Associate bot name
        System.out.println(botName);

        final LinkedList<String> inputStringMetadata = readAndSplitLine();
        gameMap.updateMap(inputStringMetadata);

        return gameMap;
    }
}
