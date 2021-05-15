using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Sister : MonoBehaviour
{
    public GameObject player;
    public GameObject potal;
    Player pp;
    ChatEvent ce;
    ChatEvent2 cs;
    Transform pt;
    float dx, dy;
    bool isTrigger = false;
    public bool createPotal = false;
    public bool check = true;

    // Start is called before the first frame update
    void Start()
    {
        pp = GameObject.Find("Player").GetComponent<Player>();
        ce = GameObject.Find("ChatBalloon").GetComponent<ChatEvent>();
        cs = GameObject.Find("ChatBalloonS").GetComponent<ChatEvent2>();
        pt = player.transform;
    }

    // Update is called once per frame
    void Update()
    {
        dx = transform.position.x - pt.position.x;
        dy = transform.position.y - pt.position.y;
        if (isTrigger == false && dx > -2 && dx < 2 && dy > -1 && dy < 1) {
            pp.isPause = true;
            pp.animator.SetBool("moving", false);
            ce.start = true;
        }
        if (pp.isPause == true && isTrigger == false) {
            if (ce.end == true) {
                cs.start = true;
                isTrigger = true;
                pp.isPause = false;
                createPotal = true;
            }
        }
    }
}
