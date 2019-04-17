package org.brapi.test.SampleOrchestratorServer.service;

import org.brapi.test.SampleOrchestratorServer.model.json.Metadata;
import org.brapi.test.SampleOrchestratorServer.model.json.MetadataPagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingUtility {
	public static void calculateMetaData(Metadata metaData) {
		int totalCount = metaData.getPagination().getTotalCount();
		int pageSize = metaData.getPagination().getPageSize();
		metaData.getPagination().setTotalPages((totalCount / pageSize) + 1);
	}

	public static Pageable getPageRequest(int page, int pageSize) {
		Metadata metaData = new Metadata().pagination(new MetadataPagination().currentPage(page).pageSize(pageSize));
		return getPageRequest(metaData, null);
	}

	public static Pageable getPageRequest(Metadata metaData) {
		return getPageRequest(metaData, null);
	}

	public static Pageable getPageRequest(Metadata metaData, Sort sortReq) {
		int page = 0;
		int pageSize = 1000;
		Sort sort = Sort.unsorted();
		
		if (metaData != null && metaData.getPagination() != null) {
			if (metaData.getPagination().getCurrentPage() != null && metaData.getPagination().getCurrentPage() >= 0) {
				page = metaData.getPagination().getCurrentPage();
			}
			if (metaData.getPagination().getPageSize() != null && metaData.getPagination().getPageSize() >= 0) {
				pageSize = metaData.getPagination().getPageSize();
			}
		}
		if (sortReq != null) {
			sort = sortReq;
		}
		return PageRequest.of(page, pageSize, sort);
	}

	public static void calculateMetaData(Metadata metaData, Page<?> page) {
		// metaData.getPagination().setPageSize(page.getNumberOfElements());
		metaData.getPagination().setCurrentPage(page.getNumber());
		metaData.getPagination().setTotalCount((int) page.getTotalElements());
		metaData.getPagination().setTotalPages((int) page.getTotalPages());
	}
}
