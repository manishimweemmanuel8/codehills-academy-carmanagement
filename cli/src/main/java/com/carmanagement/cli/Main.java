package com.carmanagement.cli;

import com.carmanagement.cli.commands.*;
import com.carmanagement.cli.http.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class Main {
    
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    
    private final Map<String, Command> commands;
    private final ApiClient client;

    public Main(String baseUrl) {
        this.client = new ApiClient(baseUrl);
        this.commands = new HashMap<>();
        
        registerCommand(new CreateCarCommand());
        registerCommand(new AddFuelCommand());
        registerCommand(new FuelStatsCommand());
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void run(String[] args) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            printHelp();
            return;
        }
        
        String commandName = args[0];
        Command command = commands.get(commandName);
        
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            System.err.println();
            printHelp();
            System.exit(1);
            return;
        }
        
        if (args.length > 1 && (args[1].equals("--help") || args[1].equals("-h"))) {
            printCommandHelp(command);
            return;
        }
        
        try {
            String[] commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
            
            command.execute(client, commandArgs);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println();
            System.err.println("Usage: " + command.getUsage());
            System.exit(1);
        } catch (ApiClient.ApiException e) {
            System.err.println("API Error: " + e.getMessage());
            if (e.getError() != null && e.getError().getFieldErrors() != null) {
                System.err.println("Field errors:");
                e.getError().getFieldErrors().forEach((field, error) -> 
                    System.err.println("  - " + field + ": " + error));
            }
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private void printHelp() {
        System.out.println("Car Management CLI");
        System.out.println("==================");
        System.out.println();
        System.out.println("Usage: java -jar cli.jar <command> [options]");
        System.out.println();
        System.out.println("Available commands:");
        System.out.println();
        
        for (Command command : commands.values()) {
            System.out.printf("  %-15s  %s%n", command.getName(), command.getDescription());
        }
        
        System.out.println();
        System.out.println("Use '<command> --help' for more information about a specific command.");
        System.out.println();
        System.out.println("Environment variables:");
        System.out.println("  API_URL    Base URL of the API server (default: http://localhost:8080)");
    }

    private void printCommandHelp(Command command) {
        System.out.println(command.getName());
        System.out.println("=".repeat(command.getName().length()));
        System.out.println();
        System.out.println(command.getDescription());
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  " + command.getUsage());
    }

    public static void main(String[] args) {
        String baseUrl = System.getenv("API_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_BASE_URL;
        }
        
        Main cli = new Main(baseUrl);
        cli.run(args);
    }
}
