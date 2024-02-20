package org.example;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // Replace "input-bucket-name" with the name of your input S3 bucket
        String inputBucketName = "cka-input-bucket";
        // Replace "output-bucket-name" with the name of your output S3 bucket
        String outputBucketName = "cka-output-bucket";
        // Replace "your-file-key" with the key of the file you want to read from the input bucket
        String fileKey = "customers.csv";

        // Create an S3 client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        // Get the object from the input S3 bucket
        S3Object s3Object = s3Client.getObject(inputBucketName, fileKey);

        // Read the object content
        try (S3ObjectInputStream inputStream = Objects.requireNonNull(s3Object.getObjectContent());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Write the content to the output S3 bucket
            String outputKey = "output-" + fileKey; // You can change the output key as desired
            s3Client.putObject(outputBucketName, outputKey, String.valueOf(reader));

            System.out.println("File copied from " + inputBucketName + "/" + fileKey + " to " + outputBucketName + "/" + outputKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
