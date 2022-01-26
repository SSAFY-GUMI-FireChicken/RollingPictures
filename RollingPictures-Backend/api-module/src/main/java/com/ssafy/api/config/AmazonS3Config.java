package com.ssafy.api.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class AmazonS3Config {

    // Amazon S3 를 접근하기 위한 AccessKey 받아오기
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    // Amazon S3 를 접근하기 위한 SecretKey 받아오기
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    // Amazon S3 를 접근하기위한 Region 값 받아오기. 아시아 태평양(서울) ap-northeast-2 임.
    @Value("${cloud.aws.region.static}")
    private String region;

    // 설정한 yml 파일에서 값을 가져와 AmazonS3Client 객체를 만들어 Bean 에 주입하는 것.
    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
