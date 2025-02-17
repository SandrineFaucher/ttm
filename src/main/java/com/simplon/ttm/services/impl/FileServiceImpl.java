package com.simplon.ttm.services.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simplon.ttm.services.FileService;

@Service
public class FileServiceImpl implements FileService {
    private final String UPLOAD_DIR = "uploads/";

    public FileServiceImpl() {
        // Crée le dossier d’upload si non existant
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String saveFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide");
        }

        // Détermine l’extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID() + fileExtension;

        // Crée le dossier si nécessaire
        Path directoryPath = Paths.get(UPLOAD_DIR, subDirectory);
        Files.createDirectories(directoryPath);

        Path filePath = directoryPath.resolve(uniqueFilename);

        // Traitement en fonction du type de fichier
        if (fileExtension.matches("\\.(jpg|jpeg|png|gif)$")) {
            // Redimensionner l’image avant de la sauvegarder
            BufferedImage resizedImage = resizeImage(file.getInputStream(), 150, 150);
            ImageIO.write(resizedImage, fileExtension.replace(".", ""), filePath.toFile());
        } else if (fileExtension.equalsIgnoreCase(".pdf")) {
            // Compresser le PDF
            compressPdf(file.getInputStream(), filePath.toFile());
        } else {
            // Sauvegarde normale pour les autres fichiers
            Files.copy(file.getInputStream(), filePath);
        }

        // Normalisation du chemin pour compatibilité web
        return filePath.toString().replace("\\", "/");
    }
    /**
     * Redimensionne une image tout en gardant les proportions
     * @param inputStream Flux d'entrée de l'image
     * @param maxWidth Largeur maximale
     * @param maxHeight Hauteur maximale
     * @return Image redimensionnée
     */
    private BufferedImage resizeImage(InputStream inputStream, int maxWidth, int maxHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);

        // Vérifier si l'image doit être redimensionnée
        if (originalImage.getWidth() <= maxWidth && originalImage.getHeight() <= maxHeight) {
            return originalImage; // Pas besoin de redimensionner
        }

        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, maxWidth, maxHeight);
    }
    /**
     * Compresse un fichier PDF en réduisant la résolution des pages
     */
    private void compressPdf(InputStream inputStream, File outputFile) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer renderer = new PDFRenderer(document);
        PDDocument compressedDoc = new PDDocument();

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 100); // Réduction de la qualité DPI
            PDPage page = new PDPage(document.getPage(i).getMediaBox());
            compressedDoc.addPage(page);
        }

        compressedDoc.save(outputFile);
        compressedDoc.close();
        document.close();
    }
}
