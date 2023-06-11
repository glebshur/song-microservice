#!/bin/bash
awslocal s3 mb s3://file-bucket
awslocal sqs create-queue --queue-name file-enricher-queue