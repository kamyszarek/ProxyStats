package com.arkaprox;

import com.arkaprox.query.QueryCounter;
import com.arkaprox.query.QueryDetails;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class DatabaseProxy {

    private static String proxyName;

    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.out.println("Usage: java -jar proxy.jar <proxyPort> <realDatabasePort>");
            System.exit(1);
        }

        proxyName = args[0];
        int proxyPort = Integer.parseInt(args[1]);
        int realDatabasePort = Integer.parseInt(args[2]);
//        int proxyPort = 5433;
//        int realDatabasePort = 5432;

        try (ServerSocket serverSocket = new ServerSocket(proxyPort)) {
            System.out.println("Database proxy listening on port " + proxyPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Socket dbSocket = new Socket("localhost", realDatabasePort);
                System.out.println("Connected to real database: " + dbSocket.getInetAddress());

                SchemaDetails schemaDetails = SchemaDetails.getInstance();
                schemaDetails.createColumnsInTablesMap();

                Thread clientToDb = new Thread(new ProxyThread(clientSocket.getInputStream(), dbSocket.getOutputStream()));
                Thread dbToClient = new Thread(new ProxyThread(dbSocket.getInputStream(), clientSocket.getOutputStream()));

                clientToDb.start();
                dbToClient.start();
            }
        }
    }

    private static class ProxyThread implements Runnable {
        private final InputStream input;
        private final OutputStream output;
        private String extractedQuery;
        QueryCounter queryCounter = QueryCounter.getInstance();

        public ProxyThread(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[4096];
                int bufferLength;
                int extractedBufferLength = 0;
                while ((bufferLength = input.read(buffer)) != -1) {
                    byte[] extractedBuffer = new byte[4096];
                    if (bufferLength > 5) {
                        int endIndex = findZeroSequenceIndex(buffer);
                        if (endIndex > 5) {
                            extractedBuffer = extractSubarray(buffer, endIndex - 1);
                            extractedBufferLength = extractedBuffer.length;
                        }
                    }
                    extractedQuery =  buildStringFromBytes(extractedBuffer, extractedBufferLength).toLowerCase(Locale.ROOT).trim();
                    QueryDetails query = new QueryDetails(extractedQuery);

                    switch (query.getQueryType()) {
                        case CREATE -> {
                            queryCounter.setCreateCount(queryCounter.getCreateCount() + 1);
                            System.out.println("Create query number: " + queryCounter.getCreateCount());
                        }
                        case DELETE -> {
                            queryCounter.setDeleteCount(queryCounter.getDeleteCount() + 1);
                            System.out.println("Delete query number: " + queryCounter.getDeleteCount());
                        }
                        case DROP -> {
                            queryCounter.setDropCount(queryCounter.getDropCount() + 1);
                            System.out.println("Drop query number: " + queryCounter.getDropCount());
                        }
                        case INSERT -> {
                            queryCounter.setInsertCount(queryCounter.getInsertCount() + 1);
                            System.out.println("Insert query number: " + queryCounter.getInsertCount());
                        }
                        case SELECT -> {
                            queryCounter.setSelectCount(queryCounter.getSelectCount() + 1);
                            System.out.println("Select query number: " + queryCounter.getSelectCount());
                        }
                        case UPDATE -> {
                            queryCounter.setUpdateCount(queryCounter.getUpdateCount() + 1);
                            System.out.println("Update query number: " + queryCounter.getUpdateCount());
                        }
                        default -> System.out.println("Unsupported query type");
                    }




                    // Select to do, which columns

//                    System.out.println("Received bytes: " + Arrays.toString(Arrays.copyOfRange(buffer, 0, bufferLength)));
//                    System.out.println("Extracted bytes: " + Arrays.toString(Arrays.copyOfRange(extractedBuffer, 0, extractedBufferLength)));
//                    System.out.println("Received data: " + buildStringFromBytes(buffer, bufferLength));
                    System.out.println("Extracted data: " + extractedQuery);





                    output.write(buffer, 0, bufferLength);
                    output.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static byte[] extractSubarray(byte[] byteArray, int endIndex) {
            int defaultQueryStartIndex = 6;
            int subarrayLength = endIndex - defaultQueryStartIndex + 1;
            byte[] subarray = new byte[subarrayLength];
            System.arraycopy(byteArray, 6, subarray, 0, subarrayLength);
            return subarray;
        }

        private static int findZeroSequenceIndex(byte[] byteArray) {
            int zeroSequenceLength = 3;
            for (int i = 5; i < byteArray.length - zeroSequenceLength + 1; i++) {
                boolean isZeroSequence = true;
                for (int j = 0; j < zeroSequenceLength; j++) {
                    if (byteArray[i + j] != 0) {
                        isZeroSequence = false;
                        break;
                    }
                }
                if (isZeroSequence) {
                    return i;
                }
            }
            return -1;
        }

        private static String buildStringFromBytes(byte[] bytes, int bytesLength) {
            StringBuilder filteredData = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                byte currentByte = bytes[i];
                if (!Character.isISOControl((char) (currentByte & 0xFF))) {
                    filteredData.append((char) (currentByte & 0xFF));
                }
            }
            return filteredData.toString();
        }

    }
}