package com.github.ka4ok85.wca.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.ka4ok85.wca.constants.JobStatus;
import com.github.ka4ok85.wca.exceptions.InternalApiMismatchException;
import com.github.ka4ok85.wca.options.JobOptions;
import com.github.ka4ok85.wca.response.JobResponse;
import com.github.ka4ok85.wca.response.ResponseContainer;

/**
 * <strong>Class for interacting with WCA GetJobStatus API.</strong> It builds
 * XML request for GetJobStatus API using
 * {@link com.github.ka4ok85.wca.options.JobOptions} and reads response into
 * {@link com.github.ka4ok85.wca.response.JobResponse}.
 * <p>
 * It relies on Spring's {@link org.springframework.web.client.RestTemplate} for
 * synchronous client-side HTTP access.
 * </p>
 *
 * @author Evgeny Makovetsky
 * @since 0.0.2
 */
public class WaitForJobCommand extends AbstractInstantCommand<JobResponse, JobOptions> {

	private static String apiMethodName = "GetJobStatus";

	/**
	 * Builds XML request for GetJobStatus API using
	 * {@link com.github.ka4ok85.wca.options.JobOptions}
	 * 
	 * @param options
	 *            - settings for API call
	 */
	@Override
	public void buildXmlRequest(JobOptions options) {
		Objects.requireNonNull(options, "JobOptions must not be null");

		Element methodElement = doc.createElement(apiMethodName);
		currentNode = addChildNode(methodElement, null);

		Element jobID = doc.createElement("JOB_ID");
		jobID.setTextContent(String.valueOf(options.getJobId()));
		addChildNode(jobID, currentNode);
	}

	/**
	 * Reads GetJobStatus API response into
	 * {@link com.github.ka4ok85.wca.response.JobResponse}
	 * 
	 * @param resultNode
	 *            - "RESULT" XML Node returned by API
	 * @param options
	 *            - settings for API call
	 * @return POJO GetJobStatus Response
	 */
	@Override
	public ResponseContainer<JobResponse> readResponse(Node resultNode, JobOptions options) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		JobResponse jobResponse = new JobResponse();
		Map<String, String> parameters = new HashMap<String, String>();
		try {
			Node jobIdNode = (Node) xpath.evaluate("JOB_ID", resultNode, XPathConstants.NODE);
			Node jobStatusNode = (Node) xpath.evaluate("JOB_STATUS", resultNode, XPathConstants.NODE);
			Node jobDescriptionNode = (Node) xpath.evaluate("JOB_DESCRIPTION", resultNode, XPathConstants.NODE);

			if (!options.getJobId().equals(Long.valueOf(jobIdNode.getTextContent()))) {
				throw new InternalApiMismatchException("Bad Job ID received from GetJobStatus API. Should have been "
						+ options.getJobId() + ", but received " + jobIdNode.getTextContent());
			}

			NodeList parametersNode = (NodeList) xpath.evaluate("PARAMETERS/PARAMETER", resultNode,
					XPathConstants.NODESET);
			Node parameterNode;

			for (int i = 0; i < parametersNode.getLength(); i++) {
				parameterNode = parametersNode.item(i);
				parameters.put(((Node) xpath.evaluate("NAME", parameterNode, XPathConstants.NODE)).getTextContent(),
						((Node) xpath.evaluate("VALUE", parameterNode, XPathConstants.NODE)).getTextContent());
			}

			jobResponse.setJobId(options.getJobId());
			jobResponse.setJobDescription(jobDescriptionNode.getTextContent());
			jobResponse.setJobStatus(JobStatus.valueOf(jobStatusNode.getTextContent()));

			jobResponse.setParameters(parameters);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseContainer<JobResponse> response = new ResponseContainer<JobResponse>(jobResponse);

		return response;
	}
}
