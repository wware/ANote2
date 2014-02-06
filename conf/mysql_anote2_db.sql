
DROP SCHEMA IF EXISTS`anote_db`;
CREATE SCHEMA IF NOT EXISTS `anote_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;

-- -----------------------------------------------------
-- Table `anote_db`.`corpus`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`corpus` (
  `idcorpus` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL DEFAULT '\\"\\"' ,
  PRIMARY KEY (`idcorpus`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`processes_type`
-- -----------------------------------------------------
CREATE  TABLE  `anote_db`.`processes_type` (
  `idprocesses_type` INT(11) NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idprocesses_type`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`processes`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`processes` (
  `idprocesses` INT(11) NOT NULL AUTO_INCREMENT ,
  `processes_type_idprocesses_type` INT(11) NOT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`idprocesses`) ,
  INDEX `fk_processes_processes_type1` (`processes_type_idprocesses_type` ASC) ,
  CONSTRAINT `fk_processes_processes_type1`
    FOREIGN KEY (`processes_type_idprocesses_type` )
    REFERENCES `anote_db`.`processes_type` (`idprocesses_type` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`publications_id_type`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`publications_id_type` (
  `id_type` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(15) NOT NULL ,
  PRIMARY KEY (`id_type`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`publications`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`publications` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `other_id` VARCHAR(20) NULL DEFAULT NULL ,
  `title` VARCHAR(400) NULL DEFAULT NULL ,
  `authors` VARCHAR(400) NULL DEFAULT NULL ,
  `type` VARCHAR(40) NULL DEFAULT NULL ,
  `date` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `fulldate` VARCHAR(25) NULL DEFAULT NULL ,
  `status` VARCHAR(25) NULL DEFAULT NULL ,
  `journal` VARCHAR(150) NULL DEFAULT NULL ,
  `volume` VARCHAR(20) NULL DEFAULT NULL ,
  `issue` VARCHAR(20) NULL DEFAULT NULL ,
  `pages` VARCHAR(75) NULL DEFAULT NULL ,
  `abstract` TEXT NULL DEFAULT NULL ,
  `external_links` TEXT NULL DEFAULT NULL ,
  `available_pdf` TINYINT(1) NULL DEFAULT '0' ,
  `publications_id_type_id_type` INT(11) NULL DEFAULT NULL ,
  `fulltext` LONGTEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_publications_publications_id_type1` (`publications_id_type_id_type` ASC) ,
  CONSTRAINT `fk_publications_publications_id_type1`
    FOREIGN KEY (`publications_id_type_id_type` )
    REFERENCES `anote_db`.`publications_id_type` (`id_type` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resources_type`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resources_type` (
  `idresources_type` INT(10) NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(25) NOT NULL ,
  PRIMARY KEY (`idresources_type`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resources`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resources` (
  `idresources` INT(10) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `resources_type_idresources_type` INT(10) NOT NULL ,
  `note` VARCHAR(150) NULL DEFAULT NULL ,
  PRIMARY KEY (`idresources`) ,
  INDEX `fk_resources_resources_type` (`resources_type_idresources_type` ASC) ,
  CONSTRAINT `fk_resources_resources_type`
    FOREIGN KEY (`resources_type_idresources_type` )
    REFERENCES `anote_db`.`resources_type` (`idresources_type` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`classes`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`classes` (
  `idclasses` INT(11) NOT NULL AUTO_INCREMENT ,
  `class` VARCHAR(90) NOT NULL ,
  PRIMARY KEY (`idclasses`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resource_elements`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resource_elements` (
  `idresource_elements` INT(10) NOT NULL AUTO_INCREMENT ,
  `resources_idresources` INT(10) NOT NULL ,
  `classes_idclasses` INT(11) NULL DEFAULT NULL ,
  `element` VARCHAR(300) NOT NULL ,
  PRIMARY KEY (`idresource_elements`, `resources_idresources`) ,
  INDEX `fk_resource_elements_resources` (`resources_idresources` ASC) ,
  INDEX `fk_resource_elements_classes1` (`classes_idclasses` ASC) ,
  CONSTRAINT `fk_resource_elements_resources`
    FOREIGN KEY (`resources_idresources` )
    REFERENCES `anote_db`.`resources` (`idresources` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_resource_elements_classes1`
    FOREIGN KEY (`classes_idclasses` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`annotations`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`annotations` (
  `idannotations` INT(11) NOT NULL AUTO_INCREMENT ,
  `processes_idprocesses` INT(11) NOT NULL ,
  `corpus_idcorpus` INT(11) NOT NULL ,
  `publications_id` INT(10) NOT NULL ,
  `start` INT(10) NOT NULL ,
  `end` INT(10) NOT NULL ,
  `resource_elements_id` INT(10) NULL DEFAULT NULL ,
  `classes_idclasses` INT(11) NULL DEFAULT NULL ,
  `element` VARCHAR(200) NULL DEFAULT NULL ,
  `normalization_form` VARCHAR(200) NULL DEFAULT NULL ,
  `type` ENUM('ner','re') NOT NULL DEFAULT 'ner' ,
  `clue` VARCHAR(200) NULL DEFAULT NULL ,
  `classification_re` VARCHAR(200) NULL DEFAULT NULL ,
  PRIMARY KEY (`idannotations`) ,
  INDEX `fk_annotations_resource_elements1` (`resource_elements_id` ASC) ,
  INDEX `fk_annotations_processes1` (`processes_idprocesses` ASC) ,
  INDEX `fk_annotations_corpus1` (`corpus_idcorpus` ASC) ,
  INDEX `fk_annotations_publications1` (`publications_id` ASC) ,
  INDEX `fk_annotations_classes1` (`classes_idclasses` ASC) ,
  CONSTRAINT `fk_annotations_corpus1`
    FOREIGN KEY (`corpus_idcorpus` )
    REFERENCES `anote_db`.`corpus` (`idcorpus` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotations_processes1`
    FOREIGN KEY (`processes_idprocesses` )
    REFERENCES `anote_db`.`processes` (`idprocesses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotations_publications1`
    FOREIGN KEY (`publications_id` )
    REFERENCES `anote_db`.`publications` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotations_resource_elements1`
    FOREIGN KEY (`resource_elements_id` )
    REFERENCES `anote_db`.`resource_elements` (`idresource_elements` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotations_classes1`
    FOREIGN KEY (`classes_idclasses` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`class_hierarchy`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`class_hierarchy` (
  `classes_idclasses` INT(11) NOT NULL ,
  `superclass` INT(11) NOT NULL ,
  INDEX `fk_class_hierarchy_classes1` (`classes_idclasses` ASC) ,
  INDEX `fk_class_hierarchy_classes2` (`superclass` ASC) ,
  CONSTRAINT `fk_class_hierarchy_classes1`
    FOREIGN KEY (`classes_idclasses` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_class_hierarchy_classes2`
    FOREIGN KEY (`superclass` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`classes_colors`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`classes_colors` (
  `classes_idclasses` INT(11) NOT NULL ,
  `color` VARCHAR(10) NULL DEFAULT '"#0000FF"' ,
  PRIMARY KEY (`classes_idclasses`) ,
  CONSTRAINT `fk_classes_colors_classes1`
    FOREIGN KEY (`classes_idclasses` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`corpus_has_processes`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`corpus_has_processes` (
  `corpus_idcorpus` INT(11) NOT NULL ,
  `processes_idprocesses` INT(11) NOT NULL ,
  PRIMARY KEY (`corpus_idcorpus`, `processes_idprocesses`) ,
  INDEX `fk_corpus_has_processes_corpus1` (`corpus_idcorpus` ASC) ,
  INDEX `fk_corpus_has_processes_processes1` (`processes_idprocesses` ASC) ,
  CONSTRAINT `fk_corpus_has_processes_corpus1`
    FOREIGN KEY (`corpus_idcorpus` )
    REFERENCES `anote_db`.`corpus` (`idcorpus` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_corpus_has_processes_processes1`
    FOREIGN KEY (`processes_idprocesses` )
    REFERENCES `anote_db`.`processes` (`idprocesses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`corpus_has_publications`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`corpus_has_publications` (
  `corpus_idcorpus` INT(11) NOT NULL ,
  `publications_id` INT(10) NOT NULL ,
  PRIMARY KEY (`corpus_idcorpus`, `publications_id`) ,
  INDEX `fk_corpus_has_publications_corpus1` (`corpus_idcorpus` ASC) ,
  INDEX `fk_corpus_has_publications_publications1` (`publications_id` ASC) ,
  CONSTRAINT `fk_corpus_has_publications_corpus1`
    FOREIGN KEY (`corpus_idcorpus` )
    REFERENCES `anote_db`.`corpus` (`idcorpus` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_corpus_has_publications_publications1`
    FOREIGN KEY (`publications_id` )
    REFERENCES `anote_db`.`publications` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`corpus_properties`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`corpus_properties` (
  `corpus_idcorpus` INT(11) NOT NULL ,
  `property_key` VARCHAR(45) NOT NULL ,
  `property_value` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`corpus_idcorpus`, `property_key`) ,
  INDEX `fk_corpus_properties_corpus1` (`corpus_idcorpus` ASC) ,
  CONSTRAINT `fk_corpus_properties_corpus1`
    FOREIGN KEY (`corpus_idcorpus` )
    REFERENCES `anote_db`.`corpus` (`idcorpus` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`document_relevance`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`document_relevance` (
  `publications_id` INT(10) NOT NULL ,
  `queries_idqueries` INT(10) UNSIGNED NOT NULL ,
  `relevance` CHAR(10) NOT NULL ,
  INDEX `document_relevance_FKIndex2` (`queries_idqueries` ASC) ,
  INDEX `fk_document_relevance_publications1` (`publications_id` ASC) ,
  CONSTRAINT `fk_document_relevance_publications1`
    FOREIGN KEY (`publications_id` )
    REFERENCES `anote_db`.`publications` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`process_properties`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`process_properties` (
  `processes_idprocesses` INT(11) NOT NULL ,
  `property_name` VARCHAR(45) NOT NULL ,
  `property_value` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`processes_idprocesses`, `property_name`) ,
  INDEX `fk_process_properties_processes1` (`processes_idprocesses` ASC) ,
  CONSTRAINT `fk_process_properties_processes1`
    FOREIGN KEY (`processes_idprocesses` )
    REFERENCES `anote_db`.`processes` (`idprocesses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`queries`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`queries` (
  `idqueries` INT(10) NOT NULL AUTO_INCREMENT ,
  `date` DATETIME NOT NULL ,
  `keywords` VARCHAR(200) NOT NULL ,
  `organism` VARCHAR(200) NULL DEFAULT NULL ,
  `matching_publications` INT(11) NULL DEFAULT NULL ,
  `available_abstracts` INT(11) NULL DEFAULT NULL ,
  `downloaded_publications` INT(11) NULL DEFAULT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`idqueries`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`queries_has_publications`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`queries_has_publications` (
  `queries_idqueries` INT(10) NOT NULL ,
  `publications_id` INT(11) NOT NULL ,
  PRIMARY KEY (`queries_idqueries`, `publications_id`) ,
  INDEX `fk_queries_has_publications_publications1` (`publications_id` ASC) ,
  INDEX `fk_queries_has_publications_queries1` (`queries_idqueries` ASC) ,
  CONSTRAINT `fk_queries_has_publications_publications1`
    FOREIGN KEY (`publications_id` )
    REFERENCES `anote_db`.`publications` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_queries_has_publications_queries1`
    FOREIGN KEY (`queries_idqueries` )
    REFERENCES `anote_db`.`queries` (`idqueries` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`queries_properties`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`queries_properties` (
  `queries_idqueries` INT(10) NOT NULL ,
  `property_name` VARCHAR(45) NOT NULL ,
  `property_value` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`queries_idqueries`, `property_name`) ,
  INDEX `fk_queries_properties_queries1` (`queries_idqueries` ASC) ,
  CONSTRAINT `fk_queries_properties_queries1`
    FOREIGN KEY (`queries_idqueries` )
    REFERENCES `anote_db`.`queries` (`idqueries` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`relation_type`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`relation_type` (
  `idrelation_type` INT(11) NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idrelation_type`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`sources`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`sources` (
  `idsources` INT(10) NOT NULL AUTO_INCREMENT ,
  `source_name` VARCHAR(30) NOT NULL ,
  PRIMARY KEY (`idsources`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resource_elements_extenal_id`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resource_elements_extenal_id` (
  `resource_elements_idresource_elements` INT(10) NOT NULL ,
  `resource_elements_resources_idresources` INT(10) NOT NULL ,
  `external_id` VARCHAR(50) NOT NULL ,
  `sources_idsources` INT(10) NULL DEFAULT NULL ,
  PRIMARY KEY (`resource_elements_idresource_elements`, `resource_elements_resources_idresources`, `external_id`) ,
  INDEX `fk_resource_elements_extenal_id_resource_elements` (`resource_elements_idresource_elements` ASC, `resource_elements_resources_idresources` ASC) ,
  INDEX `fk_resource_elements_extenal_id_sources` (`sources_idsources` ASC) ,
  CONSTRAINT `fk_resource_elements_extenal_id_resource_elements`
    FOREIGN KEY (`resource_elements_idresource_elements` , `resource_elements_resources_idresources` )
    REFERENCES `anote_db`.`resource_elements` (`idresource_elements` , `resources_idresources` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_resource_elements_extenal_id_sources`
    FOREIGN KEY (`sources_idsources` )
    REFERENCES `anote_db`.`sources` (`idsources` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resource_elements_relation`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resource_elements_relation` (
  `idresource_elements_a` INT(10) NOT NULL ,
  `idresource_elements_b` INT(10) NOT NULL ,
  `relation_type_idrelation_type` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`idresource_elements_b`, `idresource_elements_a`) ,
  INDEX `fk_resource_parent_resource_elements` (`idresource_elements_a` ASC) ,
  INDEX `fk_resource_elements_relation_relation_type` (`relation_type_idrelation_type` ASC) ,
  CONSTRAINT `fk_resource_elements_relation_relation_type`
    FOREIGN KEY (`relation_type_idrelation_type` )
    REFERENCES `anote_db`.`relation_type` (`idrelation_type` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_resource_parent_resource_elements`
    FOREIGN KEY (`idresource_elements_a` )
    REFERENCES `anote_db`.`resource_elements` (`idresource_elements` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resources_content`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resources_content` (
  `resources_idresources` INT(10) NOT NULL ,
  `classes_idclasses` INT(11) NOT NULL ,
  PRIMARY KEY (`resources_idresources`, `classes_idclasses`) ,
  INDEX `fk_resources_content_resources1` (`resources_idresources` ASC) ,
  INDEX `fk_resources_content_classes1` (`classes_idclasses` ASC) ,
  CONSTRAINT `fk_resources_content_resources1`
    FOREIGN KEY (`resources_idresources` )
    REFERENCES `anote_db`.`resources` (`idresources` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_resources_content_classes1`
    FOREIGN KEY (`classes_idclasses` )
    REFERENCES `anote_db`.`classes` (`idclasses` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`resources_elements_prioreties`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`resources_elements_prioreties` (
  `resource_elements_idresource_elements` INT(10) NOT NULL ,
  `resource_elements_resources_idresources` INT(10) NOT NULL ,
  `priorety` INT(10) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`resource_elements_idresource_elements`, `resource_elements_resources_idresources`) ,
  INDEX `fk_resources_elements_prioreties_resource_elements1` (`resource_elements_idresource_elements` ASC, `resource_elements_resources_idresources` ASC) ,
  CONSTRAINT `fk_resources_elements_prioreties_resource_elements1`
    FOREIGN KEY (`resource_elements_idresource_elements` , `resource_elements_resources_idresources` )
    REFERENCES `anote_db`.`resource_elements` (`idresource_elements` , `resources_idresources` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`synonyms`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`synonyms` (
  `resource_elements_idresource_elements` INT(10) NOT NULL ,
  `synonym` VARCHAR(250) NOT NULL ,
  PRIMARY KEY (`synonym`, `resource_elements_idresource_elements`) ,
  INDEX `fk_synonyms_copy1_resource_elements` (`resource_elements_idresource_elements` ASC) ,
  CONSTRAINT `fk_synonyms_copy1_resource_elements`
    FOREIGN KEY (`resource_elements_idresource_elements` )
    REFERENCES `anote_db`.`resource_elements` (`idresource_elements` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`annotations_properties`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`annotations_properties` (
  `idannotations` INT(11) NOT NULL ,
  `property_name` VARCHAR(100) NOT NULL ,
  `property_value` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`property_name`, `idannotations`) ,
  INDEX `fk_annotations_properties_annotations1` (`idannotations` ASC) ,
  CONSTRAINT `fk_annotations_properties_annotations1`
    FOREIGN KEY (`idannotations` )
    REFERENCES `anote_db`.`annotations` (`idannotations` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`publication_fields`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`publication_fields` (
  `publications_id` INT(10) NOT NULL ,
  `field` VARCHAR(50) NOT NULL ,
  `start` INT(10) NOT NULL ,
  `end` INT(10) NOT NULL ,
  PRIMARY KEY (`field`, `publications_id`) ,
  INDEX `fk_publications_fields_publications1` (`publications_id` ASC) ,
  CONSTRAINT `fk_publications_fields_publications1`
    FOREIGN KEY (`publications_id` )
    REFERENCES `anote_db`.`publications` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


-- -----------------------------------------------------
-- Table `anote_db`.`annotations_side`
-- -----------------------------------------------------
CREATE  TABLE `anote_db`.`annotations_side` (
  `idannotations` INT(11) NOT NULL ,
  `idannotations_sub` INT(11) NOT NULL ,
  `type` ENUM('left','right') NOT NULL ,
  PRIMARY KEY (`idannotations`, `idannotations_sub`) ,
  INDEX `fk_annotations_side_annotations1` (`idannotations` ASC) ,
  INDEX `fk_annotations_side_annotations2` (`idannotations_sub` ASC) ,
  CONSTRAINT `fk_annotations_side_annotations1`
    FOREIGN KEY (`idannotations` )
    REFERENCES `anote_db`.`annotations` (`idannotations` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotations_side_annotations2`
    FOREIGN KEY (`idannotations_sub` )
    REFERENCES `anote_db`.`annotations` (`idannotations` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;
