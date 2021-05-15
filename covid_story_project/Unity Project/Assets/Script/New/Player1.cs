using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Player1 : MonoBehaviour
{
    float speed = 3f;
    float jumpPower = 7f;
    int isColli = 0;

    Rigidbody2D rigidbody;
    public Animator animator;
    Vector3 movement;
    SpriteRenderer spriteRenderer;
    public Transform pos;
    public Vector2 boxSize;

    public GameObject weaponcollider;

    public Slider hpslider;

    bool isJump = false;
    public bool isAttack = false;
    public bool isPause = false;

    bool isDie = false;
    public int Maxhp = 10;
    public int hp;
    // Start is called before the first frame update
    void Start()
    {
        rigidbody = GetComponent<Rigidbody2D>();
        animator = GetComponent<Animator>();
        spriteRenderer = GetComponent<SpriteRenderer>();
        hp = Maxhp;
        //hp = PlayerPrefs.GetInt("hp");
    }


    // Update is called once per frame
    void Update()
    {
        hpslider.maxValue = Maxhp;
        hpslider.value = hp;

        if (isDie && Input.GetKeyDown(KeyCode.Space)) {
            PlayerPrefs.DeleteAll();
            SceneManager.LoadScene("House");
            Time.timeScale = 1.0f;
        }
        if (hp == 0){
    		if(!isDie)
    			Die();
    		return;
    	}
        if (Input.GetButtonDown ("Jump") && isColli == 0 && isPause == false) {
            isJump = true;
            animator.SetBool("jump", true);
            animator.SetBool("moving", false);
        }
        if (Input.GetKeyDown (KeyCode.X) && isPause == false) {
            //Debug.Log("Att");
            isAttack = true;
            weaponcollider.SetActive (true);
            animator.SetBool("moving", false);
        }
        if (animator.GetCurrentAnimatorStateInfo(0).IsName("Hit") && animator.GetCurrentAnimatorStateInfo(0).normalizedTime >= 1f)
        {
            isAttack = false;
            weaponcollider.SetActive(false);
            animator.SetBool("attack", false);
        }
    }

    void Move() {
        Vector3 moveVelocity = Vector3.zero;

        if (Input.GetAxisRaw("Horizontal") < 0) {
            moveVelocity = Vector3.left;
            transform.localScale = new Vector2(1, 1);
            if (isColli != 1)
                animator.SetBool("moving", true);
        }
        else if (Input.GetAxisRaw("Horizontal") > 0) {
            moveVelocity = Vector3.right;
            transform.localScale = new Vector2(-1, 1);
            if (isColli != 1)
                animator.SetBool("moving", true);
        }
        else {
            animator.SetBool("moving", false);
        }
        transform.position += moveVelocity.normalized * speed * Time.deltaTime;
    }

    void Jump() {
        if (!isJump || isColli != 0) return;

        rigidbody.velocity = Vector2.zero;
        Vector2 jumpVelocity = new Vector2 (0, jumpPower);
        rigidbody.AddForce (jumpVelocity, ForceMode2D.Impulse);
        
        isJump = false;
    }

    void Attack() {
        if(!isAttack || isColli != 0) return;
        animator.SetBool("attack", true);/*
        Collider2D collider2Ds = Physics2D.OverlapBoxAll (pos.position, boxSize, 0);
        foreach (Collider2D collider in collider2Ds){
            if(collider.tag == "Monster"){
                collider.GetComponent<Monster>().TakeDamage(1);
            }
        }*/
    }

    public void WeaponcolliderOnOff(){
        //weaponcollider.SetActive (!weaponcollider.activeInHierarchy);
    }

    void FixedUpdate()
    {
        if (isPause == false) {
            Move();
            Jump();
            Attack();
            Debug.DrawRay(rigidbody.position, Vector3.down, new Color(0, 1, 0));
            RaycastHit2D rayHit = Physics2D.Raycast(rigidbody.position, Vector3.down, 1, LayerMask.GetMask("Platform"));
            if(rayHit.collider != null){
                //if(rayHit.distance <0.5f)
                //Debug.Log(rayHit.collider.name);
            }
        }
    }

    void OnCollisionEnter2D(Collision2D collision)
	{
        isColli = 2;
        animator.SetBool("jump", false);
        if(collision.gameObject.tag == "Monster"){
            OnDamaged(collision.transform.position);
        }
	}

	void OnCollisionStay2D(Collision2D collision)
	{
        isColli = 0;
	}

    void OnDamaged(Vector2 targetPos){
        gameObject.layer = 11;
        hp--;
        Debug.Log(hp);
        spriteRenderer.color = new Color(1, 1, 1, 0.4f);

        int dir = transform.position.x - targetPos.x > 0 ? 1 : -1;
        rigidbody.AddForce(new Vector2(dir,1)*3, ForceMode2D.Impulse);

        Invoke("OffDamaged", 1);
    }
    void OffDamaged(){
        gameObject.layer = 9;
        spriteRenderer.color = new Color(1, 1, 1);
    }

    void Die(){
    	isDie = true;
    	rigidbody.velocity = Vector2.zero;

		GameObject.Find("Canvas").transform.Find("GameOverPanel").gameObject.SetActive(true);

		Time.timeScale = 0;
    }

	//물리적 충돌에서 벗어났을 때
	void OnCollisionExit2D(Collision2D collision)
	{
        isColli = 1;
	}

    void OnTriggerStay2D(Collider2D col) {
        if (col.tag == "Potal") {
            if (Input.GetAxisRaw("Vertical") > 0) {
                if (SceneManager.GetActiveScene().name == "House") SceneManager.LoadScene("FrontOfHouse");
                else if (SceneManager.GetActiveScene().name == "FrontOfStation") SceneManager.LoadScene("Subway");
                else if (SceneManager.GetActiveScene().name == "Subway") SceneManager.LoadScene("SubwayEnd");
                else if (SceneManager.GetActiveScene().name == "Railroad") SceneManager.LoadScene("Shelter");
                else if (SceneManager.GetActiveScene().name == "Shelter") SceneManager.LoadScene("HallwayLab");
                else if (SceneManager.GetActiveScene().name == "HallwayLab") SceneManager.LoadScene("Lab");
            }
        }
    }
}
