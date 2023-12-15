package com.arkaprox.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryDetails {

    private final QueryType queryType;

    public QueryDetails(String query) {
        this.queryType = checkQueryType(query);
    }

    public static QueryType checkQueryType(String query) {
        if (query.startsWith("select")) {
            return QueryType.SELECT;
        } else if (query.startsWith("insert")) {
            return QueryType.INSERT;
        } else if (query.startsWith("update")) {
            return QueryType.UPDATE;
        } else if (query.startsWith("delete")) {
            return QueryType.DELETE;
        } else if (query.startsWith("create")) {
            return QueryType.CREATE;
        } else if (query.startsWith("drop")) {
            return QueryType.DROP;
        } else if (query.startsWith("alter")) {
            return QueryType.ALTER;
        } else {
            return QueryType.UNKNOWN;
        }
    }

    public static void main(String[] args) {
        String query = "INSERT   INTO \"Kontrahenci\" (\"ImięKontrahenta\", \"NazwiskoKontrahenta\", \"Województwo\") VALUES ('Klaudia', 'Dawidowska', 'ZPM');";
        String query2 = "insert into pasniki_info (nr, karma, wielkosc, nr_lasu) values (343254, 'siano', 23, 101);";

        System.out.println(extractColumns(query2));
        System.out.println(extractTableName(query2));
    }

    private static List<String> extractColumns(String query) {
        String pattern = "insert\\s+into\\s+(.*?)+\\((.*?)\\)";
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(query);
        List<String> columnsList = new ArrayList<>();
        if (matcher.find()) {
            String columns = matcher.group(2);
            String[] columnsArray = columns.split("\\s*,\\s*");
            for (String column : columnsArray) {
                String cleanedColumn = column.trim().replaceAll("^\"|\"$", "");
                columnsList.add(cleanedColumn);
            }
        }
        return columnsList;
    }

    private static String extractTableName(String query) {
        String pattern = "insert\\s+into\\s+?(\\\"?\\w+\\\"?\\.?\\\"?\\w+\\\"?)";
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(query);
        if (matcher.find()) {
            String tableName = matcher.group(1).trim();
            tableName = tableName.replaceAll("\"", "");
            return tableName;

        } else {
            return "Nie znaleziono pasującego wzorca.";
        }
    }

    public QueryType getQueryType() {
        return queryType;
    }
}
