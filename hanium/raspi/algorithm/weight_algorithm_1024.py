import random
balance = 12
it = 0
#lst = [10, 10, 1, 1, 5, 10]
order = []
w_lst = []
layer = 1
temp_line = 0 # 0, 1, 2
empty = True
full = False
stop = False

def need_rearrange(temp):
    left = 0
    right = 0
    for i in range(len(order)):
        if order[i] == 1:
            left += w_lst[i]
        elif order[i] == 3:
            right += w_lst[i]
    if abs(left - right) > balance:
        return True
    else:
        return False

def move_item(temp):
    '''
    do rearrange for load balancing
    '''
    print("rearrange")
    tmp_weight = w_lst[len(w_lst)-1] # save weight temporarily
    del temp[len(temp)-1]
    del w_lst[len(w_lst)-1]
    
    if w_lst[len(temp)-1] > w_lst[len(temp)-2]:
        temp[len(temp)-2] = 6 - temp[len(temp)-1] - temp[len(temp)-2]
        
    elif w_lst[len(temp)-1] < w_lst[len(temp)-2]:
        temp[len(temp)-1] = 6 - temp[len(temp)-1] - temp[len(temp)-2]
        
    else:
        if temp[len(temp)-1] == 1 or temp[len(temp)-1] == 3:
            temp[len(temp)-1] = 6 - temp[len(temp)-1] - temp[len(temp)-2]
        else:
            temp[len(temp)-2] = 6 - temp[len(temp)-1] - temp[len(temp)-2]

    # actual move item action in 'HERE'
    # rearrange item to the position of last element of 'order'
    
    temp.append(6 - temp[len(temp)-1] - temp[len(temp)-2])  # save position
    w_lst.append(tmp_weight)    # save weight
    return temp, w_lst

def get_min_pos(temp):
    left = 0
    center = 0
    right = 0
    for i in range(len(order)):
        if order[i] == 1:
            left += w_lst[i]
        elif order[i] == 2:
            center += w_lst[i]
        else:
            right += w_lst[i]
    if min(left, center, right) == left:
        return 1
    elif min(left, center, right) == center:
        return 2
    else:
        return 3

def comp_two(temp):
    left = 0
    center = 0
    right = 0
    for i in range(len(order)):
        if order[i] == 1:
            left += w_lst[i]
        elif order[i] == 2:
            center += w_lst[i]
        else:
            right += w_lst[i]
    if min(left, center, right) == left: # compare center, right
        if center >= right:
            return 3
        else:
            return 2
    elif min(left, center, right) == center: # compare left, right
        if left > right:
            return 3
        else:
            return 1
    else: # compare left, center
        if left <= center:
            return 1
        else:
            return 2

while not stop:
    # get weight
#    weight = lst[it]
#    it += 1
    weight = random.randint(1,10)
    
    # empty case
    if empty:
        if weight <= 5:
            order.append(1)
            w_lst.append(weight)
        else:
            order.append(2)
            w_lst.append(weight)
        empty = False
        temp_line += 1
        temp_line %= 3

    # non empty case
    else:
        if not full: # current layer is not full, 1 item case, 2 item case
            if temp_line == 1: # 1 item case
                if get_min_pos(order) == 1:
                    if order[len(order) - 1] == 1:
                        order.append(2)
                    else:
                        order.append(1)
                    w_lst.append(weight)
                elif get_min_pos(order) == 2:
                    if order[len(order) - 1] == 2:
                        if comp_two(order) == 1:
                            order.append(1)
                        else:
                            order.append(3)
                    else:
                        order.append(2)
                    w_lst.append(weight)
                else:
                    if order[len(order) - 1] == 3:
                        order.append(2)
                    else:
                        order.append(3)
                    w_lst.append(weight)
                temp_line += 1
                temp_line %= 3
            else: # 2 item case
                order.append(6 - order[len(order) -1] - order[len(order) -2])
                w_lst.append(weight)
                temp_line += 1
                temp_line %= 3
                layer += 1
                full = True
        else: # current layer is full, 3 item case
            full = False
            order.append(get_min_pos(order))
            w_lst.append(weight)
            temp_line += 1
            temp_line %= 3
    
    # check need rearrange
    if need_rearrange(order):
        move_item(order)
        
    # actual move item action in 'HERE'
    # move item to the position of last element of 'order'

    # check loading is finished
    if len(order) == 6:
        stop = True

print(order)
print(w_lst)
print(need_rearrange(order))
