# Use the official Nginx image to serve the HTML file
FROM nginx:latest

# Copy the HTML file to the default Nginx HTML directory
COPY src/test/resources/loan_calculation_system.html /usr/share/nginx/html/index.html

# Expose port 80 to allow access to Nginx
EXPOSE 80
