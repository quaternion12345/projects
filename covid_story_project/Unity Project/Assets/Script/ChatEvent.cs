using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChatEvent : MonoBehaviour
{
    public GameObject player;
    Chat1 cp;
    Chat2 cp2;
    Chat3 cp3;
    Chat4 cp4;
    float x, y, z;
    Transform pt;

    public bool start = false; 
    public bool end = false;
    bool ing = false;
    // Start is called before the first frame update
    void Start()
    {
        x = transform.position.x;
        y = transform.position.y;
        z = transform.position.z;
        pt = player.transform;
        cp = GameObject.Find("Chat1").GetComponent<Chat1>();
        cp2 = GameObject.Find("Chat2").GetComponent<Chat2>();
        cp3 = GameObject.Find("Chat3").GetComponent<Chat3>();
        cp4 = GameObject.Find("Chat4").GetComponent<Chat4>();
    }

    // Update is called once per frame
    void Update()
    {
        if (start == true) {
            transform.position = new Vector3(pt.position.x+0.2f, pt.position.y+0.7f, pt.position.z);
            start = false;
            ing = true;
            cp.start = true;
        }
        if (ing == true) {
            if (Input.GetKeyUp(KeyCode.Space) && cp.change == false) {
                cp.change = true;
                cp2.start = true;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && cp2.change == false) {
                cp2.change = true;
                cp3.start = true;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && cp3.change == false) {
                cp3.change = true;
                cp4.start = true;
            }
            else if (Input.GetKeyUp(KeyCode.Space) && cp4.change == false) {
                cp4.change = true;
            }
        }
        if (cp.end == true && cp2.end == true && cp3.end == true && cp4.end == true) {
            ing = false;
            end = true;
            transform.position = new Vector3(x, y, z);
        }
    }
}
