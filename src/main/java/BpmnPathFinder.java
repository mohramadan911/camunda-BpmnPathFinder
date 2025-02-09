import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class BpmnPathFinder {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar BpmnPathFinder.jar startNodeId endNodeId");
            System.exit(-1);
        }

        String startId = args[0];
        String endId = args[1];

        try {
            String bpmnXml = fetchBpmnXml();
            BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(bpmnXml.getBytes()));

            FlowNode startNode = (FlowNode) modelInstance.getModelElementById(startId);
            FlowNode endNode = (FlowNode) modelInstance.getModelElementById(endId);

            if (startNode == null) {
                System.err.println("Start node '" + startId + "' not found.");
                System.exit(-1);
            }
            if (endNode == null) {
                System.err.println("End node '" + endId + "' not found.");
                System.exit(-1);
            }

            List<String> path = findPath(startNode, endNode);

            if (path == null) {
                System.err.println("No path found from " + startId + " to " + endId + ".");
                System.exit(-1);
            }

            System.out.println("The path from " + startId + " to " + endId + " is:");
            System.out.println(path);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String fetchBpmnXml() throws IOException {
        URL url = new URL("https://n35ro2ic4d.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP request failed with code: " + responseCode);
        }

        try (InputStream inputStream = conn.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(inputStream);
            return root.get("bpmn20Xml").asText();
        }
    }

    private static List<String> findPath(FlowNode start, FlowNode end) {
        Queue<List<FlowNode>> queue = new LinkedList<>();
        queue.add(Collections.singletonList(start));

        while (!queue.isEmpty()) {
            List<FlowNode> path = queue.poll();
            FlowNode currentNode = path.get(path.size() - 1);

            if (currentNode.getId().equals(end.getId())) {
                return path.stream()
                        .map(FlowNode::getId)
                        .collect(Collectors.toList());
            }

            for (SequenceFlow sequenceFlow : currentNode.getOutgoing()) {
                FlowNode target = sequenceFlow.getTarget();
                if (!path.contains(target)) {
                    List<FlowNode> newPath = new ArrayList<>(path);
                    newPath.add(target);
                    queue.add(newPath);
                }
            }
        }

        return null;
    }
}