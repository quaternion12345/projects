using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class TexiDriver : MonoBehaviour
{
    public GameObject player;
    Player pp;
    Transform pt;
    ChatEvent3 ce3;
    ChatEvent4 ce4;
    float dx, dy;
    bool isTrigger = false;

    // Start is called before the first frame update
    void Start()
    {
        pp = GameObject.Find("Player").GetComponent<Player>();
        ce3 = GameObject.Find("ChatBalloonN").GetComponent<ChatEvent3>();
        ce4 = GameObject.Find("ChatBalloonP").GetComponent<ChatEvent4>();
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
            ce3.start = true;
            isTrigger = true;
        }
        if (pp.isPause == true && isTrigger == true) {
            if (ce3.end == true && ce4.end == true) {
                pp.isPause = false;
                SceneManager.LoadScene("FrontOfStation");
            }
        }
    }
}
