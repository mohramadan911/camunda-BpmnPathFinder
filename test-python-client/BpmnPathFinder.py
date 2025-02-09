import requests
import xml.etree.ElementTree as ET

def fetch_and_parse_bpmn():

    url = "https://n35ro2ic4d.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml"
    response = requests.get(url)
    bpmn_xml = response.json()['bpmn20Xml']

    tree = ET.ElementTree(ET.fromstring(bpmn_xml))
    root = tree.getroot()
    
    ns = {'bpmn': 'http://www.omg.org/spec/BPMN/20100524/MODEL'}
    
    print("\nAll nodes with IDs:")
    print("-----------------")
    for element in root.findall(".//bpmn:*", ns):
        node_id = element.get("id")
        if node_id:
            print(f"Type: {element.tag.split('}')[1]}, ID: {node_id}")
    
    print("\nSequence flows:")
    print("--------------")
    for flow in root.findall(".//bpmn:sequenceFlow", ns):
        source = flow.get("sourceRef")
        target = flow.get("targetRef")
        print(f"From {source} -> To {target}")

if __name__ == "__main__":
    fetch_and_parse_bpmn()