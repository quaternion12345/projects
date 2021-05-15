using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SpawnManager : MonoBehaviour
{
	public Transform[] points;
	public GameObject monsterPrefab;

	public float createTime;
	public int maxMonster;
    // Start is called before the first frame update
    void Start()
    {
     	points = GameObject.Find("SpawnPoint").GetComponentsInChildren<Transform>();
        if(points.Length> 0){
            StartCoroutine(this.CreateMonster());
        }   
    }

    IEnumerator CreateMonster(){
    	while(true){
    		int monsterCount = (int)GameObject.FindGameObjectsWithTag("Monster").Length;
    		if(monsterCount < maxMonster){
    			yield return new WaitForSeconds(createTime);

    			int idx = Random.Range(1, points.Length);

    			GameObject obj = (GameObject)Instantiate(monsterPrefab, points[idx].position, points[idx].rotation);
                obj.SetActive(true);
    		}
            yield return null;
    	}
    }

    // Update is called once per frame
    void Update()
    {
    }
}
