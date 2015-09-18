package com.cumulativeminds.zeus.infra;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;

@Component
public class AwsUtil {

    private AmazonEC2 amazonEC2;

    public AwsUtil() {
        this.amazonEC2 = new AmazonEC2Client();
    }

    public List<String> findMemberBy(String awsTagKey, String awsTagValue) {
        List<String> members = new ArrayList<>();
        DescribeInstancesRequest req = new DescribeInstancesRequest()
                .withFilters(new Filter()
                        .withName("tag:" + awsTagKey)
                        .withValues(awsTagValue));

        DescribeInstancesResult describeInstancesResult = amazonEC2.describeInstances(req);

        describeInstancesResult.getReservations().forEach(r -> {
            r.getInstances().stream()
                    // filter the instance which do not have private IPs
                    .filter(i -> i.getPrivateIpAddress() != null)
                    .map(i -> i.getPrivateIpAddress())
                    .forEach(i -> members.add(i));

        });

        return members;
    }

}
