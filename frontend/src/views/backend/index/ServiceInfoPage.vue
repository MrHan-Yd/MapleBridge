<script setup>
import { ref } from "vue";
import { get } from "@/net/http";
import {ElWarning} from "@/util/MessageUtil";

const data = ref({
  javaVersion: "",
  mariadbVersion: "",
  elasticsearchVersion: "",
  elasticsearchAnalyzer: "",
  redisVersion: "",
  rabbitmqErlangOTPVersion: "",
  rabbitmqVersion: "",
  kafkaVersion: "",
  mongodbVersion: "",
});

function getData() {
  get("api/backend/service-info", (rs) => {
    if (rs.code === 200) {
      data.value = rs.data;
    } else {
      ElWarning(rs.message) ;
    }
  }) ;
}
getData() ;
</script>

<template>
  <div id="service-info">
    <el-descriptions title="服务信息(缓存，有效期30分钟)" :column="3" border>
      <el-descriptions-item
          label="Java版本"
          label-align="right"
          align="center"
          label-class-name="my-label"
          class-name="my-content"
          width="150px"
      >
        <el-tag size="small">{{data.javaVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="持久层数据库" label-align="right" align="center">
        <el-tag size="small">{{data.mariadbVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="ElasticSearch版本" label-align="right" align="center">
        <el-tag size="small">{{data.elasticsearchVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="Elasticsearch分词器" label-align="right" align="center">
        <el-tag size="small">{{data.elasticsearchAnalyzer}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="Redis版本" label-align="right" align="center">
        <el-tag size="small">{{data.redisVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="RabbitMQ Erlang/OTP版本" label-align="right" align="center">
        <el-tag size="small">{{data.rabbitmqErlangOTPVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="RabbitMQ版本" label-align="right" align="center">
        <el-tag size="small">{{data.rabbitmqVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="Kafka版本" label-align="right" align="center">
        <el-tag size="small">{{data.kafkaVersion}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="MongoDB版本" label-align="right" align="center">
        <el-tag size="small">{{data.mongodbVersion}}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<style scoped>
#service-info {
  height: 86.5vh;
  margin-right: 7px;
  margin-bottom: 10px;
  background-color: white;
  padding-top: 10px;
  padding-left: 10px;
  padding-right: 10px;
}
</style>