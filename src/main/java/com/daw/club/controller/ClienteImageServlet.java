package com.daw.club.controller;

import com.daw.club.AppConfig;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.logging.Logger;

/**
 * The Image servlet for serving from absolute path.
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/04/imageservlet.html
 * @note access url /clienteimg/file.jpg
 */
@WebServlet("/clienteimg/*")
public class ClienteImageServlet extends HttpServlet {

    // Properties ---------------------------------------------------------------------------------

    @Inject
    private AppConfig appConfig;

    private String imagesPath;

    static final Logger log= Logger.getLogger(ClienteImageServlet.class.getName());

    @Override
    public void init()  {

        // Define base path somehow. Samples:
        //this.imagePath = "/tmp/images";  //Server absolute path, e.g. c:/tmp/images (windows)
        //this.imagePath = System.getProperty("user.home")+"/images";  //User home relative path /home/username/images
        this.imagesPath=appConfig.getProperty("app.data")+appConfig.getProperty("customer.images");
    }

    // Actions ------------------------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get image file name from url, i.r. /clienteimg/imagefile.jpg
        String requestedImage = request.getPathInfo();

        if (requestedImage==null) {
            // Do your thing if the image is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Decode the file name (might contain spaces and on) and prepare file object.
        File imageFile = new File(imagesPath, URLDecoder.decode(requestedImage, "UTF-8"));

        // Check if file actually exists in filesystem.
        if (!imageFile.exists()) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            log.warning(String.format("File %s not found", imageFile.getName()));
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(imageFile.getName());

        // Check if file is actually an image (avoid download of other files by hackers!).
        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
        if (contentType == null || !contentType.startsWith("image")) {
            // Do your thing if the file appears not being a real image.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            log.warning(String.format("File %s is not an image", imageFile.getName()));
            return;
        }
        log.info(String.format("GET %s image", imageFile.getName()));
        // Init servlet response.
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(imageFile.length()));

        // Write image content to response.
        Files.copy(imageFile.toPath(), response.getOutputStream());
    }

}