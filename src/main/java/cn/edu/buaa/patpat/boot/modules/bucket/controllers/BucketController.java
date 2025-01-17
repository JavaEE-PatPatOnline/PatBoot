/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.bucket.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.bucket.services.BucketService;
import cn.edu.buaa.patpat.boot.modules.bucket.services.PathService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/bucket")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bucket", description = "Bucket service that allows user to upload files to the server")
public class BucketController {
    private final AuthApi authApi;
    private final BucketService bucketService;
    private final PathService pathService;

    @PostMapping("upload/public")
    @Operation(summary = "Upload file to individual-public bucket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully, returned data is the URL of the file"),
            @ApiResponse(responseCode = "400", description = "No file uploaded"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Failed to save file")
    })
    @ValidateMultipartFile(maxSize = 10)
    @ValidatePermission
    public DataResponse<String> uploadPublic(
            MultipartFile file,
            AuthPayload auth
    ) {
        String record;
        try {
            record = bucketService.saveToPublic(auth.getBuaaId(), file.getOriginalFilename(), file);
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new InternalServerErrorException();
        }

        return DataResponse.ok(
                M("bucket.upload.success"),
                pathService.recordToUrl(record));
    }
}
