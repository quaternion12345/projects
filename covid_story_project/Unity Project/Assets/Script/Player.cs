using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Player : MonoBehaviour
{
    float speed = 3f;
    float jumpPower = 7f;
    int isColli = 0;

    Rigidbody2D rigidbody;
    public Animator animator;
    Vector3 movement;
    bool isJump = false;
    public bool isPause = false;
    // Start is called before the first frame update
    void Start()
    {
        rigidbody = GetComponent<Rigidbody2D>();
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetButtonDown ("Jump") && isColli == 0 && isPause == false) {
            isJump = true;
            animator.SetBool("jump", true);
            animator.SetBool("moving", false);
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

    void FixedUpdate()
    {
        if (isPause == false) {
            Move();
            Jump();
        }
        else {

        }
    }

    void OnCollisionEnter2D(Collision2D collision)
	{
        isColli = 0;
        animator.SetBool("jump", false);
	}

	void OnCollisionStay2D(Collision2D collision)
	{
        isColli = 0;
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
