package cn.edu.buaa.patpat.boot.modules.bucket.services;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.modules.bucket.config.BucketOptions;
import cn.edu.buaa.patpat.boot.modules.bucket.exceptions.MalformedPathException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

/**
 * The service to handle the path of the content.
 * There are two types of content: public and private.
 * <ul>
 *     <li>Public content: The content that can be accessed by everyone directly via URL.</li>
 *     <li>Private content: The content that can only be accessed by the owner or the authorized users.</li>
 * </ul>
 * Each content also contains two types: individual and shared.
 * <ul>
 *     <li>Individual content: Each user has its own individual path to store the content.</li>
 *     <li>Shared content: All users share the same path to store the content.</li>
 * </ul>
 * To make the storage more convenient, we only store the essential path of the content in the database.
 * The essential path is the relative path of the content in the bucket, which is called record.
 * <ul>
 *      <li>For individual content, the path is {tag}/{filename}.</li>
 *      <li>For shared content, the path is {filename}.</li>
 * </ul>
 * Then by adding prefix to the record, we can get the physical path and network URL of the content.
 */
@Service
@RequiredArgsConstructor
public class PathService {
    private final BucketOptions bucketOptions;
    @Value("${config.http}")
    private String httpUrl;

    /**
     * Get the record of the content in the database.
     *
     * @param tag      The tag of the content. If is null or empty, will
     *                 be stored to the shared bucket.
     * @param filename The filename of the content.
     * @return The record of the content.
     */
    public String toRecord(String tag, String filename) {
        if (Strings.isNullOrEmpty(tag)) {
            return toRecord(filename);
        }
        return Paths.get(tag, filename).toString();
    }

    /**
     * Get the record of the public content in the database.
     *
     * @param filename The filename of the content.
     * @return The record of the content.
     */
    public String toRecord(String filename) {
        return filename;
    }

    /**
     * Get the private path of the content.
     *
     * @param record The record of the content.
     * @return The private path of the content.
     */
    public String recordToPrivatePath(String record) {
        return Paths.get(bucketOptions.getPrivateRoot(), record).toString();
    }

    /**
     * Get the URL of the content. Of course, only public record has URL.
     *
     * @param record The record of the content.
     * @return The URL of the content.
     */
    public String recordToUrl(String record) {
        return httpUrl + "/public/" + record;
    }

    /**
     * Get the public path of the content.
     *
     * @param record The record of the content.
     * @return The public path of the content.
     */
    public String recordToPublicPath(String record) {
        return Paths.get(bucketOptions.getPublicRoot(), record).toString();
    }

    /**
     * Get the record from the path.
     *
     * @param path The path of the content.
     * @return The record of the content.
     * @throws MalformedPathException If the path is invalid.
     */
    public String pathToRecord(String path) throws MalformedPathException {
        if (path.startsWith(bucketOptions.getPublicRoot())) {
            return path.substring(bucketOptions.getPublicRoot().length() + 1);
        } else if (path.startsWith(bucketOptions.getPrivateRoot())) {
            return path.substring(bucketOptions.getPrivateRoot().length() + 1);
        } else {
            throw new MalformedPathException("Invalid path: " + path);
        }
    }

    /**
     * Get the record from the URL.
     *
     * @param url The URL of the content.
     * @return The record of the content.
     * @throws MalformedPathException If the URL is invalid.
     */
    public String url2Record(String url) throws MalformedPathException {
        if (!url.startsWith(httpUrl)) {
            throw new MalformedPathException("Invalid URL: " + url);
        }
        return url.substring(httpUrl.length() + 1);
    }
}