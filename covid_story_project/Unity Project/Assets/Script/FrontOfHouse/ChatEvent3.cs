using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChatEvent3 : MonoBehaviour
{
    public GameObject npc;
    ChatEvent4 ce4;
    ChatN1 c1;
    ChatN2 c2;
    ChatN3 c3;
    float x, y, z;
    Transform nt;
    public int count = 0;

    public bool start = false; 
    public bool end = false;
    bool ing = false;
    // Start is called before the first frame update
    void Start()
    {
        x = transform.position.x;
        y = transform.position.y;
        z = transform.position.z;
        nt = npc.transform;
        ce4 = GameObject.Find("ChatBalloonP").GetComponent<ChatEvent4>();
        c1 = GameObject.Find("ChatN1").GetComponent<ChatN1>();
        c2 = GameObject.Find("ChatN2").GetComponent<ChatN2>();
        c3 = GameObject.Find("ChatN3").GetComponent<ChatN3>();
    }

    // Update is called once per frame
    void Update()
    {
        if (start == true) {
            transform.position = new Vector3(nt.position.x+0.2f, nt.position.y+0.7f, nt.position.z);
            start = false;
            ing = true;
            c1.start = true;
        }
        if (ing == true) {
            if (Input.GetKeyUp(KeyCode.Space) && ce4.count == 0) {
                c1.change = true;
                transform.position = new Vector3(x, y, z);
                ce4.start = true;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && ce4.count == 1) {
                transform.position = new Vector3(nt.position.x+0.2f, nt.position.y+0.7f, nt.position.z);
                c2.start = true;
                count++;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && ce4.count == 2) {
                c2.change = true;
                transform.position = new Vector3(x, y, z);
                count++;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && ce4.count == 3) {
                transform.position = new Vector3(nt.position.x+0.2f, nt.position.y+0.7f, nt.position.z);
                c3.start = true;
                count++;
            }
            if (Input.GetKeyUp(KeyCode.Space) && ce4.end == true) {
                c3.change = true;
                transform.position = new Vector3(x, y, z);
                ing = false;
                end = true;
            }
        }
    }
}
