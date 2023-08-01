package shgo.innowise.trainee.songmicroservice.fileapi.service.strategy;

import org.springframework.stereotype.Service;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;

import java.util.HashMap;
import java.util.Map;

/**
 * Matches StorageType enum values with StorageStrategy instances.
 */
@Service
public class StorageStrategyRegistry {
    private Map<StorageType, StorageStrategy> storageStrategyMap = new HashMap<>();

    /**
     * Get storage strategy by enum value.
     *
     * @param storageType handler type
     * @return storage strategy
     */
    public StorageStrategy getStorageStrategy(StorageType storageType) {
        return storageStrategyMap.get(storageType);
    }

    /**
     * Adds new storage strategy to registry.
     *
     * @param type     storage type
     * @param strategy storage strategy
     */
    public void register(StorageType type, StorageStrategy strategy) {
        storageStrategyMap.put(type, strategy);
    }
}
