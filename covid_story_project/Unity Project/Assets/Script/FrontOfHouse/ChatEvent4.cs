using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChatEvent4 : MonoBehaviour
{
    public GameObject npc;
    ChatEvent3 ce3;
    ChatP1 c1;
    ChatP2 c2;
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
        ce3 = GameObject.Find("ChatBalloonN").GetComponent<ChatEvent3>();
        c1 = GameObject.Find("ChatP1").GetComponent<ChatP1>();
        c2 = GameObject.Find("ChatP2").GetComponent<ChatP2>();
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
            if (Input.GetKeyUp(KeyCode.Space) && ce3.count == 0) {
                c1.change = true;
                transform.position = new Vector3(x, y, z);
                count++;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && ce3.count == 1) {
                transform.position = new Vector3(nt.position.x+0.2f, nt.position.y+0.7f, nt.position.z);
                c2.start = true;
                count++;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && ce3.count == 2) {
                c2.change = true;
                transform.position = new Vector3(x, y, z);
                count++;
            }
            else if (ce3.count == 3) {
                ing = false;
                end = true;
            }
        }
    }
}
