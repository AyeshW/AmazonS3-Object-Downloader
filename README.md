# AmazonS3-Object-Downloader
This is a simple project which can be used to download objects in a bucket iterating trough directories.

This project supposed that you have a AmazonS3 bucket with a structure like below.

.
+-- bucket-name
|   +-- production
    |   +-- domain-logs
        |   +-- abanse(domain name)
        |   |   +-- sub (only 1 sub directory)
        |       |   +-- january (all months of the year)
        |       |   |   +-- 2018-01-10 (logs per day in the month)
        |       |   |   |   +-- file1.log
        |       |   |   |   +-- file2.log
        |       |   |   +-- 2018-01-11
        |       |   |   |   +-- file1.log
        |       |   |   |   +-- file2.log
        |       |   |   +-- 2019-01-10
        |       |       |   +-- file1.log
        |       |       |   +-- file2.log
        |       |   +-- february
        |           |   +-- 2018-02-10
        |           |   |   +-- file1.log
        |           |   |   +-- file2.log
        |           |   +-- 2018-02-11
        |           |   |   +-- file1.log
        |           |   |   +-- file2.log
        |           |   +-- 2019-02-10
        |               |   +-- file1.log
        |               |   +-- file2.log
        |   +-- singer
        |   +-- softlogic
             
