using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChatEvent2 : MonoBehaviour
{
    public GameObject sister;
    ChatS1 cs1;
    ChatS2 cs2;
    float x, y, z;
    Transform st;

    public bool start = false;

    private float TimeLeft = 1.0f;
    private float T1 = 0.0f;
    private float T2 = 0.0f;

    // Start is called before the first frame update
    void Start()
    {
        x = transform.position.x;
        y = transform.position.y;
        z = transform.position.z;
        st = sister.transform;
        cs1 = GameObject.Find("ChatS1").GetComponent<ChatS1>();
        cs2 = GameObject.Find("ChatS2").GetComponent<ChatS2>();
    }

    // Update is called once per frame
    void Update()
    {
        if (start == false) {
            if (Time.time > T1) {
                transform.position = new Vector3(st.position.x+0.7f, st.position.y+0.5f, st.position.z);
                cs1.start = true;
                T1 = Time.time + 3.0f*TimeLeft;
                T2 = Time.time + TimeLeft;
            }
            else if (Time.time > T2) {
                transform.position = new Vector3(x, y, z);
                cs1.change = true;
            }
        }
        else {
            transform.position = new Vector3(st.position.x+0.7f, st.position.y+0.5f, st.position.z);
            cs1.change = true;
            cs2.start = true;
        }
    }
}
