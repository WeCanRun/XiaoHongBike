package com.chb.share_bike.config;

import com.chb.share_bike.pojo.Bike;
import com.chb.share_bike.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

public class MongoInitConfig {
    @Autowired
    private MongoTemplate template;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        IndexOperations userIndexOps = template.indexOps(User.class);
        userIndexOps.ensureIndex(new Index().on("phoneNum", Sort.Direction.ASC));

        IndexOperations bikeIndexOps = template.indexOps(Bike.class);
        bikeIndexOps.ensureIndex(new Index().on("bikeNo", Sort.Direction.ASC));
        MongoPersistentEntityIndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
        resolver.resolveIndexFor(Bike.class).forEach(bikeIndexOps::ensureIndex);
    }
}
