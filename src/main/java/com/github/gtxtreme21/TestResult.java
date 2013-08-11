package com.github.gtxtreme21;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestResult {
	
	private String fileName;
	private List<SamplerResult> samplerResults = new ArrayList<SamplerResult>();
	boolean passed = true;
	
	public TestResult(String fileName){
		this.fileName = fileName;
	}

	public void addSamplerResult(String samplerName, long time, boolean passed, String response, List<String> failedAssertions){
		SamplerResult result = new SamplerResult();
		result.samplerName = samplerName;
		result.time = time;
		result.passed = passed;
		result.samplerResponse = response;
		result.failedAssertions = failedAssertions;
		samplerResults.add(result);
		if(!passed){
			this.passed = false;
		}
	}
	
	public String toHtml(){
		String id = UUID.randomUUID().toString();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<div><div style='font-weight:bold; cursor:pointer;");
		if(this.passed){
			sb.append("color:#008000;");
		}
		else{
			sb.append("color:#FF0000;");
		}
		sb.append("' title='Click to show/hide sampler results.'' onclick='toggleContents(\"").append(id).append("\");'>");
		
		sb.append(fileName).append(" (Passed: ").append(passed).append(")");
		sb.append("</div>");
		sb.append("<div id='").append(id).append("'");
		if(passed){
			sb.append(" style='display:none'");
		}
		sb.append(">");
		
		sb.append("<ul>");
		for(SamplerResult result : samplerResults){
			sb.append("<li>").append(result.toHtml()).append("</li>");
		}
		sb.append("</ul>");
		
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private static class SamplerResult{
		private String samplerName;
		private long time;
		private boolean passed;
		private String samplerResponse;
		private List<String> failedAssertions = new ArrayList<String>();
		
		public String toHtml(){
			StringBuilder sb = new StringBuilder();
			sb.append("<span style='color:");
			if(this.passed){
				sb.append("#008000");
			}
			else{
				sb.append("#FF0000");
			}
			sb.append(";'>");
			
			sb.append(samplerName);
			sb.append(" (").append(time).append("ms)");
			sb.append("</span>");
			
			if(samplerResponse != null){
				sb.append("<p>Sampler Response: ").append(samplerResponse).append("</p>");
			}
			if(failedAssertions != null && !failedAssertions.isEmpty()){
				sb.append("<ul>");
				for(String failedAssertion : failedAssertions){
					sb.append("<li>Failed Assertion - ");
					sb.append(failedAssertion);
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
			
			return sb.toString();
		}
	}

	/**
	 * @return the passed
	 */
	public boolean isPassed() {
		return passed;
	}
}
