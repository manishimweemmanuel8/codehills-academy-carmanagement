package com.carmanagement;

import com.carmanagement.repository.CarRepository;
import com.carmanagement.repository.FuelRepository;
import com.carmanagement.service.CarService;
import com.carmanagement.service.FuelService;
import com.carmanagement.servlet.CarsApiServlet;
import com.carmanagement.servlet.FuelStatsServlet;
import com.carmanagement.servlet.HealthServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Application {
    
    private static final int DEFAULT_PORT = 8080;
    
    private final Server server;
    private final int port;

    public Application() {
        this(DEFAULT_PORT);
    }
    
    public Application(int port) {
        this.port = port;
        this.server = createServer();
    }

    private Server createServer() {
        Server server = new Server(port);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        CarRepository carRepository = new CarRepository();
        FuelRepository fuelRepository = new FuelRepository();
        
        CarService carService = new CarService(carRepository);
        FuelService fuelService = new FuelService(fuelRepository, carRepository);
        
        CarsApiServlet carsApiServlet = new CarsApiServlet(carService, fuelService);
        context.addServlet(new ServletHolder(carsApiServlet), "/api/cars");
        context.addServlet(new ServletHolder(carsApiServlet), "/api/cars/*");
        
        FuelStatsServlet fuelStatsServlet = new FuelStatsServlet(fuelService);
        context.addServlet(new ServletHolder(fuelStatsServlet), "/servlet/fuel-stats");
        
        HealthServlet healthServlet = new HealthServlet();
        context.addServlet(new ServletHolder(healthServlet), "/health");
        
        return server;
    }

    public void start() throws Exception {
        server.start();
        System.out.println("========================================");
        System.out.println("  Car Management Server Started");
        System.out.println("  Port: " + port);
        System.out.println("========================================");
        System.out.println();
        System.out.println("Available endpoints:");
        System.out.println("  POST   /api/cars                  - Create a car");
        System.out.println("  GET    /api/cars                  - List all cars");
        System.out.println("  POST   /api/cars/{id}/fuel        - Add fuel entry");
        System.out.println("  GET    /api/cars/{id}/fuel/stats  - Get fuel statistics");
        System.out.println("  GET    /servlet/fuel-stats?carId={id} - Manual servlet");
        System.out.println("  GET    /health                     - Health check");
        System.out.println();
        System.out.println("Press Ctrl+C to stop the server");
        System.out.println("========================================");
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println("Using default port: " + DEFAULT_PORT);
            }
        }
        
        String envPort = System.getenv("PORT");
        if (envPort != null) {
            try {
                port = Integer.parseInt(envPort);
            } catch (NumberFormatException e) {
                System.err.println("Invalid PORT environment variable: " + envPort);
            }
        }
        
        try {
            Application app = new Application(port);
            app.start();
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\nShutting down server...");
                    app.stop();
                    System.out.println("Server stopped.");
                } catch (Exception e) {
                    System.err.println("Error during shutdown: " + e.getMessage());
                }
            }));
            
            app.join();
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
