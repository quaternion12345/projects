using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ChatN1 : MonoBehaviour
{
    public GameObject chatBalloon;
    Transform ct;
    public bool change = false;
    public bool start = false;
    public bool end = false;
    float x,y,z;

    // Start is called before the first frame update
    void Start()
    {
        ct = chatBalloon.transform;
        x = transform.position.x;
        y = transform.position.y;
        z = transform.position.z;
    }

    // Update is called once per frame
    void Update()
    {
        if (start == true) {
            start = false;
            transform.position = new Vector3 (ct.position.x, ct.position.y, ct.position.z);
        }
        if (change == true) {
            transform.position = new Vector3 (x, y, z);
            end = true;
        }
    }
}
