using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraCtrl : MonoBehaviour
{
    public GameObject A;
    Transform AT;
    void Start ()
    {
        AT=A.transform;
    }
    void Update () {
        transform.position = new Vector3 (AT.position.x,0,transform.position.z);
    }
}
