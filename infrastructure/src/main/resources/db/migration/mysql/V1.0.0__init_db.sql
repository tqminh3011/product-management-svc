-- `product_owner` TABLE --
CREATE TABLE IF NOT EXISTS `product_owner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `contact` VARCHAR(50) DEFAULT NULL,
    `location` VARCHAR(50) DEFAULT NULL,
    `created_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `updated_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `creator` VARCHAR(50) DEFAULT NULL,
    `updator` VARCHAR(50) DEFAULT NULL,
    `version` INT UNSIGNED DEFAULT 0 NOT NULL,
    INDEX idx_owner_name(`name`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 collate = utf8mb4_unicode_ci;

-- insert `product_owner` record for testing
INSERT INTO `product_owner` (`name`, `contact`, `location`) values ('owner1', '+84 769957718', 'Vietnam');

-- `product` TABLE --
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `external_id` VARCHAR(100) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `product_owner_id` BIGINT NOT NULL,
    `price` DECIMAL(30,2) NOT NULL,
    `active` TINYINT(1) NOT NULL,
    `created_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `updated_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `creator` VARCHAR(50) DEFAULT NULL,
    `updator` VARCHAR(50) DEFAULT NULL,
    `version` INT UNSIGNED DEFAULT 0 NOT NULL,
    FOREIGN KEY (`product_owner_id`) REFERENCES `product_owner`(`id`),
    UNIQUE (`external_id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 collate = utf8mb4_unicode_ci;

-- `deleted_product` TABLE (moving products to this after user deletion) --
CREATE TABLE IF NOT EXISTS `deleted_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `product_owner_id` BIGINT NOT NULL,
    `price` DECIMAL(30,2) NOT NULL,
    `created_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `updated_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `creator` VARCHAR(50) DEFAULT NULL,
    `updator` VARCHAR(50) DEFAULT NULL,
    `version` INT UNSIGNED DEFAULT 0 NOT NULL,
    PRIMARY KEY(`id`, `created_date`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 collate = utf8mb4_unicode_ci
PARTITION BY HASH( MONTH(`created_date`) ) PARTITIONS 12;

-- `product_tracking` TABLE --
CREATE TABLE IF NOT EXISTS `product_tracking` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` VARCHAR(100) DEFAULT NULL,
    `product_owner_id` BIGINT NOT NULL,
    `activity` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `metadata` TEXT DEFAULT NULL,
    `created_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `updated_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3),
    `creator` VARCHAR(50) DEFAULT NULL,
    `updator` VARCHAR(50) DEFAULT NULL,
    INDEX idx_product(`product_id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 collate = utf8mb4_unicode_ci;
