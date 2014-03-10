package com.paperight.product.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.paperight.ProductImportService;

@Service
public class ProductImportSeviceBatchImpl implements ProductImportService {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("productImportJob")
	private Job job;
	
	
	@Override
	public void importProductsFromFile(String filename) throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("input.file.name", filename);			
		JobParameters jobParameters = jobParametersBuilder.toJobParameters();
		jobLauncher.run(job, jobParameters);
	}

	
	
}
