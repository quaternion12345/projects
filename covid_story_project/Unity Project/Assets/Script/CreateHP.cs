using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CreateHP : MonoBehaviour
{
    public int Maxhp = 10;
    Player1 player;

    // Start is called before the first frame update
    void Start()
    {
        PlayerPrefs.DeleteAll();
        PlayerPrefs.SetInt("hp", Maxhp);
        player = GameObject.Find("Player1").GetComponent<Player1>();
    }

    // Update is called once per frame
    void Update()
    {
        PlayerPrefs.SetInt("hp", player.hp);
    }
}
