"""
The original code is from https://github.com/dennybritz/reinforcement-learning/tree/master/TD
"""

import sys
import numpy as np
import itertools
import pickle
from collections import defaultdict
from game import Game

# In our case, we have 3 action (stay, go-left, go-right)
def get_action_num():
    return 3


## this function return policy function to choose the action based on Q value.
def make_policy(Q, epsilon, nA):
    """
    This is the epsilon-greedy policy, which select random actions for some chance (epsilon).
    (Check dennybritz's repository for detail)

    You may change the policy function for the given task.
    """
    def policy_fn(observation):        
        A = np.ones(nA, dtype=float) * epsilon / nA
        best_action = np.argmax(Q[observation])
        A[best_action] += (1.0 - epsilon)
        return A
    return policy_fn
def reachability(basket, item):
    # function for calculate minimum_move to get item
    orientation = 0 # 0 : stay / 1 : left / 2 : right
    # check reachability for item
    if item[1] == 0:
        min_move = abs(basket - 0)
        if basket - 0 > 0:
            orientation = 1
    elif item[1] == 1:
        min_move = min(abs(basket-0), abs(basket-1))
        if basket - 1 > 0:
            orientation = 1
        elif basket - 0 < 0:
            orientation = 2
    elif item[1] == 2:
        min_move = min(abs(basket-0), abs(basket-2))
        if basket - 2 > 0:
            orientation = 1
        elif basket - 0 < 0:
            orientation = 2
    elif item[1] == 3:
        min_move = min(abs(basket-1), abs(basket-2))
        if basket - 2 > 0:
            orientation = 1
        elif basket - 1 < 0:
            orientation = 2
    elif item[1] == 4:
        min_move = min(abs(basket-2), abs(basket-3))
        if basket - 3 > 0:
            orientation = 1
        elif basket - 2 < 0:
            orientation = 2
    elif item[1] == 5:
        min_move = min(abs(basket-3), abs(basket-4))
        if basket - 4 > 0:
            orientation = 1
        elif basket - 3 < 0:
            orientation = 2
    elif item[1] == 6:
        min_move = min(abs(basket-3), abs(basket-5))
        if basket - 5 > 0:
            orientation = 1
        elif basket - 3 < 0:
            orientation = 2
    elif item[1] == 7:
        min_move = min(abs(basket-4), abs(basket-5))
        if basket - 5 > 0:
            orientation = 1
        elif basket - 4 < 0:
            orientation = 2
    elif item[1] == 8:
        min_move = min(abs(basket-5), abs(basket-6))
        if basket - 6 > 0:
            orientation = 1
        elif basket - 5 < 0:
            orientation = 2
    elif item[1] == 9:
        min_move = min(abs(basket-6), abs(basket-7))
        if basket - 7 > 0:
            orientation = 1
        elif basket - 6 < 0:
            orientation = 2
    elif item[1] == 10:
        min_move = min(abs(basket-6), abs(basket-8))
        if basket - 8 > 0:
            orientation = 1
        elif basket - 6 < 0:
            orientation = 2
    else:
        min_move = min(abs(basket-7), abs(basket-8))
        if basket - 8 > 0:
            orientation = 1
        elif basket - 7 < 0:
            orientation = 2
    return [min_move, orientation]

def calculate_importance(basket_loc, coin_loc, time_loc):
    # importance weight for each state; stay / left / right
    value = [0,0,0]
    reachable_coin = []
    reachable_time = []
    # filter reachable coins
    for item in coin_loc:
        if reachability(basket_loc, item)[0] <= (9-item[2]) :
            reachable_coin.append(item)
    # calculate weight with reachable items
    # count reachable item positions and maximum distance from basket for each orientation
    on_item = 0
    left_item = 0
    right_item = 0
    left_distance = 0
    right_distance = 0
    for item in reachable_coin:
        move, orientation = reachability(basket_loc, item)
        if orientation == 1:
            left_item += 1
            left_distance = max(left_distance, move)
        elif orientation == 2:
            right_item += 1
            right_distance = max(right_distance, move)
        else:
            on_item += 1
            if 9-item[2] == 1: # item is directly above the basket
                value[0] += 500
    # give penalty to each orientation using maximum distance
    value[0] += on_item * 5
    value[1] += left_item * 5 - (left_distance)
    value[2] += right_item * 5 - (right_distance)
    # if basket is on side, slightly weight on move to center
    if basket_loc < 3:
        value[1] -= 3
        value[0] -= 1
    elif basket_loc > 5:
        value[2] -= 3
        value[0] -= 1
    # add weight with reachable time
    # filter reachable time
    for item in time_loc:
        if reachability(basket_loc, item)[0] <= (9-item[2]):
            reachable_time.append(item)
    for item in reachable_time:
        move, orientation = reachability(basket_loc, item)
        if move == 0:
            if 9-item[2] == 1: # item is directly above basket
                value[0] += 500
        else:
            if orientation == 1:
                value[1] += 200
            elif orientation == 2:
                value[2] += 200
    return value

## this function return state from given game information.
def get_state(counter, score, game_info):
    basket_location, item_location = game_info
    """
    FILL HERE!
    you can (and must) change the return value.
    """
    # basket_location = (x), item_location = [(id,x1,y1), (id,x2,y2), ... ]
    # need to overvalue the center position
    # 0 : stay / 1 : left / 2: right
    coin_location = [x for x in item_location if x[0] == 1]
    time_location = [x for x in item_location if x[0] == 2]
    weight = calculate_importance(basket_location, coin_location, time_location)
    maximum = max(weight[0], weight[1], weight[2])
    if maximum == weight[1]:
        return "1"
    elif maximum == weight[2]:
        return "2"
    else:
        return "0"

## this function return reward from given previous and current score and counter.
def get_reward(prev_score, current_score, prev_counter, current_counter):
    """
    FILL HERE!
    you can (and must) change the return value.
    """
    # current_score - prev_score = 0 or 500 or -150
    # current_counter - prev_counter = -1 or 99
    score_weight = (current_score - prev_score) // 100
    if (current_counter > 500) or (current_score > 300000):
        counter_weight = 0
    else:
        if (current_counter - prev_counter) > 0:
            counter_weight = 50
        else:
            counter_weight = 0
    return score_weight + counter_weight


def save_q(Q, num_episode, params, filename="model_q.pkl"):
    data = {"num_episode": num_episode, "params": params, "q_table": dict(Q)}
    with open(filename, "wb") as w:
        w.write(pickle.dumps(data))

        
def load_q(filename="model_q.pkl"):
    with open(filename, "rb") as f:
        data = pickle.loads(f.read())
        return defaultdict(lambda: np.zeros(3), data["q_table"]), data["num_episode"], data["params"]


def q_learning(game, num_episodes, params):
    """
    Q-Learning algorithm: Off-policy TD control. Finds the optimal greedy policy
    while following an epsilon-greedy policy.
    You can edit those parameters, please speficy your changes in the report.
    
    Args:
        game: Coin drop game environment.
        num_episodes: Number of episodes to run for.
        discount_factor: Gamma discount factor.
        alpha: TD learning rate.
        epsilon: Chance the sample a random action. Float betwen 0 and 1.
    
    Returns:
        Q: the optimal action-value function, a dictionary mapping state -> action values.
    """
    
    epsilon, alpha, discount_factor = params
    
    # The final action-value function.
    # A nested dictionary that maps state -> (action -> action-value).
    Q = defaultdict(lambda: np.zeros(get_action_num()))  
    
    # The policy we're following
    policy = make_policy(Q, epsilon, get_action_num())
    
    for i_episode in range(num_episodes):
        # Reset the environment and pick the first action
        _, counter, score, game_info = game.reset()
        state = get_state(counter, score, game_info)
        action = 0
        
        # One step in the environment
        for t in itertools.count():
            # Take a step
            action_probs = policy(get_state(counter, score, game_info))
            action = np.random.choice(np.arange(len(action_probs)), p=action_probs)
            done, next_counter, next_score, game_info = game.step(action)
            
            next_state = get_state(counter, score, game_info)
            reward = get_reward(score, next_score, counter, next_counter)
            
            counter = next_counter
            score = next_score
            
            """
            this code performs TD Update. (Update Q value)
            You may change this part for the given task.
            """
            best_next_action = np.argmax(Q[next_state])    
            td_target = reward + discount_factor * Q[next_state][best_next_action]
            td_delta = td_target - Q[state][action]
            Q[state][action] += alpha * td_delta
                
            if done:
                break
                
            state = next_state
        
        # Print out which episode we're on, useful for debugging.
        if (i_episode + 1) % 100 == 0:
            print("Episode {}/{} (Score: {})\n".format(i_episode + 1, num_episodes, score), end="")
            sys.stdout.flush()

    return Q

def train(num_episodes, params):
    g = Game(False)
    Q = q_learning(g, num_episodes, params)
    return Q


## This function will be called in the game.py
def get_action(Q, counter, score, game_info, params):
    epsilon = params[0]
    policy = make_policy(Q, epsilon, 3)
    action_probs = policy(get_state(counter, score, game_info))
    action = np.random.choice(np.arange(len(action_probs)), p=action_probs)
    return action

def main():
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", "--num_episode", help="# of the episode (size of training data)",
                    type=int, required=True)
    parser.add_argument("-e", "--epsilon", help="the probability of random movement, 0~1",
                    type=float, default=0.1)
    parser.add_argument("-lr", "--learning_rate", help="learning rate of training",
                    type=float, default=0.1)
    
    args = parser.parse_args()
    
    if args.num_episode is None:
        parser.print_help()
        exit(1)

    # you can pass your parameter as list or dictionary.
    # fix corresponding parts if you want to change the parameters
    
    num_episodes = args.num_episode
    epsilon = args.epsilon
    learning_rate = args.learning_rate
    
    Q = train(num_episodes, [epsilon, learning_rate, 0.5])
    save_q(Q, num_episodes, [epsilon, learning_rate, 0.5])
    
    Q, n, params = load_q()

if __name__ == "__main__":
    main()