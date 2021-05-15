using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Potal : MonoBehaviour
{
    public int createPotal = 0;

    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {
        if (createPotal == 1) {
            transform.position = new Vector3 (-3.58f, -1.03f, -1.5f);
        }
    }
}
