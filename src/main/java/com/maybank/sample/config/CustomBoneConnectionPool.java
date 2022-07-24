package com.maybank.sample.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.jolbox.bonecp.BoneCPDataSource;

@ConditionalOnProperty(name = "entity.connectionpool.provider", havingValue = "bonecp", matchIfMissing = false)
public class CustomBoneConnectionPool extends BoneCPDataSource {

}
