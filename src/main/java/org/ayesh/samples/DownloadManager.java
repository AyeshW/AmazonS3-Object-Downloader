package org.ayesh.samples;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class DownloadManager {

    private static String s3AccessKey = "XXXXXXXXXXXXXX";
    private static String s3SecretKey = "XXXXXXXXXXXXXXXXXXXXXXX";
    private AmazonS3 s3Client;
    private String bucket;

    public DownloadManager(String bucket) {
        this.bucket = bucket;
    }

    public static void setAccessSecretKeys(String s3AccessKey, String s3SecretKey) {
        DownloadManager.s3AccessKey = s3AccessKey;
        DownloadManager.s3SecretKey = s3SecretKey;
    }

    public String getBucket() {
        return bucket;
    }

    private AWSCredentials getCredentials(){
        return new BasicAWSCredentials(
                DownloadManager.s3AccessKey,
                DownloadManager.s3SecretKey
        );
    }

    private void setS3Client(){
        AWSCredentials credentials = this.getCredentials();
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    public void download(){
        // String bucket_name = "singerplus";
        // Name of each month should be capitalize when working with the production
        setS3Client();

        String[] monthNames = {"january", "february",
                "march", "april", "may", "june", "july",
                "august", "september", "october", "november",
                "december"};

        Calendar now = Calendar.getInstance();
        String year = String.valueOf(now.get(Calendar.YEAR));
        String month = String.valueOf(now.get(Calendar.MONTH)+1);
        String day = String.valueOf(now.get(Calendar.DATE));
        String date = year + "-" + month + "-" + day;
        String monthName = monthNames[now.get(Calendar.MONTH)];

        String domainPrefix = "production/domain-logs/";
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(this.bucket)
                .withPrefix(domainPrefix)
                .withDelimiter("/");

        List<String> objects = this.s3Client.listObjects(listObjectsRequest).getCommonPrefixes();
        for (String upToTenant : objects){
            // In case of changing the hierarchy of the bucket structure, newPrefix should be changed
            String newPrefix = upToTenant + "sub/" + monthName + "/" + date + "/";
            String domain = upToTenant.split("/")[2];
            boolean flag = true;
            for (S3ObjectSummary summary : S3Objects.withPrefix(this.s3Client, this.bucket, newPrefix)) {
                if (flag){
                    flag = false;
                }else {
                    String key = summary.getKey();
                    System.out.println(key);
                    String regionName = key.split("/")[6];
                    S3Object s3object = this.s3Client.getObject(this.bucket, key);
                    S3ObjectInputStream inputStream = s3object.getObjectContent();
                    try {
                        String destFileName = "/home/ayesh/IdeaProjects/amazonS3-downloader/src/main/resources/samples/" + date + "_" + domain + "_" + regionName;
                        FileUtils.copyInputStreamToFile(inputStream, new File(destFileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
