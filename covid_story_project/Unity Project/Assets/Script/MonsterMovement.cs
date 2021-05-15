using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MonsterMovement : MonoBehaviour
{
    Rigidbody2D rigid;
    Animator anim;
    SpriteRenderer sr;
    int nextMove;
    bool isTracing=false;
    Transform player;
    float distance;
    bool check=false;
    int cur;
    float base_speed = 1.0f;
    bool isDie=false;
    public float trace_dis;
    public float movespeed;
    public int hp;
    public float die;
    public bool isBoss;
    private float DTime = 0.6f;
    private float TTime = 0;

    void Awake(){
        rigid = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        sr = GetComponent<SpriteRenderer>();
        nextMove = Random.Range(-1,2);
        sr.flipX = nextMove == 1;
        player = GameObject.FindGameObjectWithTag("Player").transform;

        cur = hp;
        Invoke("ChangeMove",5);
    }

    void Update(){
//    	navAgent.SetDestination(target.position);
    }

    void FixedUpdate(){
        rigid.velocity = new Vector2(nextMove*base_speed, rigid.velocity.y);
        distance = GetDistanceFromaPlayer();
        //MoveToTarget();
        //Invoke("OnDamaged",1);
        if(!isDie){
	        if(check){
	        	check = false;
	        	anim.SetBool("isHit",false);
            }
            if(TTime < DTime){
                rigid.velocity = Vector2.zero;
                TTime += Time.deltaTime;
            }
	    	if(isBoss){
	 			Vector2 frontVec = new Vector2(rigid.position.x + nextMove*0.1f, rigid.position.y);
		        Debug.DrawRay(frontVec, Vector3.down, new Color(0, 1, 0));
		        RaycastHit2D rayHit = Physics2D.Raycast(frontVec, Vector3.down, 1, LayerMask.GetMask("Platform"));
		       // if(rayHit.collider == null)
		     	//	Turn();
	    	}
	    	else{
		        Vector2 frontVec = new Vector2(rigid.position.x + nextMove*0.4f, rigid.position.y);
		        Debug.DrawRay(frontVec, Vector3.down, new Color(0, 1, 0));
		        RaycastHit2D rayHit = Physics2D.Raycast(frontVec, Vector3.down, 1, LayerMask.GetMask("Platform"));
		        if(rayHit.collider == null)
		        	Turn();
	        }
	        if(distance < trace_dis && isTracing== false){
	        	base_speed = movespeed;
	        	//Debug.Log("distance"+distance);
	        	CancelInvoke();
	        	MoveToPlayer();
	        	isTracing = true;
	        }
    	}	
    }
    
    void MoveToPlayer(){
		//Debug.Log(player.position.x+"   "+transform.position.x);

    	if(distance >= trace_dis){
    		isTracing = false;
    		base_speed = 1.0f;
    		CancelInvoke();
    		Invoke("ChangeMove",5);
    		return;
    	}
    	TurnToDestination();
    	MoveToDestination();
    	Invoke("MoveToPlayer",1);
    }
    void ChangeMove(){
        nextMove = Random.Range(-1, 2);
        float nextChange = Random.Range(2, 5);
        Invoke("ChangeMove",nextChange);

        anim.SetInteger("isMove", nextMove);
        if(nextMove != 0)   
            sr.flipX = nextMove == 1;
    }

    void Turn(){
    	nextMove *= -1;
        sr.flipX = nextMove == 1;
    	CancelInvoke();
    	Invoke("ChangeMove",5);
    }

    float GetDistanceFromaPlayer(){
    	float distance = Vector3.Distance(transform.position, player.position);
    	return distance;
    }

    void TurnToDestination(){

    	if(player.position.x-transform.position.x >0){
    		nextMove = 1;
    	}
    	else{
    		nextMove = -1;
    	}
    	sr.flipX = nextMove == 1;
    }

    void MoveToDestination(){
        anim.SetInteger("isMove", nextMove);
    }
/*
    void OnDamaged(Collider2D col){
    	if(col.CompareTag("Player")){
	    		if(p.isAttack){
		    	anim.SetBool("isHit", true);
		    	cur --;
		    	if(cur <= 0){
		    		Invoke("Die",3);
		    	}
	    	}
    	}
    	return;
    }*/

    public void TakeDamage(int dam){
    	cur -= dam;
    	anim.SetBool("isHit", true);

    	if(cur==0)Die();
    	else
    	    StartCoroutine (WaitFor());

        TTime = 0;
    	//hitBoxCollider.SetActive (false);
    }

    public void OnTriggerEnter2D (Collider2D colli){
    	if (colli.transform.CompareTag ("weapon")){
			TakeDamage(1);
    	}
    }

    void Die(){
    	anim.SetBool("isHit",false);
    	isDie = true;
    	anim.SetBool("isDie", true);
    	gameObject.layer = 13;
    	Destroy(this.gameObject, die);
    }


    IEnumerator WaitFor(){
    	if(!isBoss)yield return new WaitForSeconds (0.3f);
    	else yield return new WaitForSeconds (0.5f);
    	check = true;
    }
}