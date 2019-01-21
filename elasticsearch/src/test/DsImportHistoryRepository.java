package com.example.elasticsearch.dao;

import com.example.elasticsearch.entity.DsImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DsImportHistoryRepository extends JpaRepository<DsImportHistory, String> {

}
