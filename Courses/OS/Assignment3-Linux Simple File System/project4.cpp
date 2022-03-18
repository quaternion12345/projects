#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <queue>
#include <stack>
using namespace std;

// global variables
int id_assingn_number = 0; // counter for assign ids
int total_used_block = 0;  // total used block number
queue<int> deleted_ids;

class dentry{
public:
	char name[64]; // dentry name (directory name)

	class dentry* parent;		// parent dentry		
	vector<class dentry*> d_dentry; // a list of children dentries
	vector<class inode*> d_inode; // a list of inodes
	// number of child dentry and files	
	dentry(){}
	dentry(char name[], dentry* parent) {
		// constructor
		strcpy(this->name, name);
		this->parent = parent;
	}
	dentry(const dentry& s) {
		// copy constructor
		strcpy(this->name, s.name);
		this->parent = s.parent;
		this->d_dentry.assign(s.d_dentry.begin(), s.d_dentry.end());
		this->d_inode.assign(s.d_inode.begin(), s.d_inode.end());
	}
};
class inode {
public:
	int id;			// inode ID(0~127)
	char name[64];	// inode name (file name)
	int size;		// file size

//	void* direct_block[12];	// point 12 direct blocks
//	void* single_indirect;	// point 1 disk block and it points 256 disk blocks
//	void* double_indirect;  // double indirect 1 -> 256 -> 256 * 256
	// number of specific type disk blocks
	int db_num = 0; // disk block number
	int sib_num = 0;// disk block number + 1
	int dib_num = 0;// disk block number + indirect block number + 1
	inode(char name[], int size) {
		// constructor
		strcpy(this->name, name);
		this->size = size;
		int allocateblocks = 0;
		if (size % 1024 == 0)
			allocateblocks = size / 1024;
		else
			allocateblocks = size / 1024 + 1;
		if (allocateblocks <= 12) { // less than 12KB
			this->db_num = allocateblocks;
			this->sib_num = 0;
			this->dib_num = 0;
		}
		else if (allocateblocks <= 268) {
			this->db_num = 12;
			this->sib_num = allocateblocks - 12 + 1;
			this->dib_num = 0;
		}
		else {
			this->db_num = 12;
			this->sib_num = 256 + 1;
			this->dib_num = allocateblocks - 268;
			if (this->dib_num % 256 == 0) // second indirect
				this->dib_num += (this->dib_num / 256);
			else
				this->dib_num += (this->dib_num / 256) + 1;
			this->dib_num += 1; // first indirect
		}		
		// update global data
		if(deleted_ids.empty())
			this->id = id_assingn_number++;		
		else {
			this->id = deleted_ids.front();
			deleted_ids.pop();
		}
		total_used_block += this->db_num + this->sib_num + this->dib_num;
	}
};
class super_block {
public:
	class dentry* s_root;	// parent dentry -> pointing root dentry
};

// global variables
inode *inode_table[128]; // inode table for control inodes

int student_id = 2016147538;	// student id
super_block superblock;			// super block

dentry* current_directory;		// directory of current directory
string current_dir = "/";		// current directory location
bool Terminate = false;
int recovery_counter = 0;
stack<string> recovery_name;


// function proto types
void Ls();
void Cd(string input);
void Mkdir(vector<string> names);
void Rmdir(vector<string> names, dentry* CD);
void Mkfile(string name, int size);
void Rmfile(vector<string> names, dentry* CD);
void Inode(string name);
void recovery();

int main(void) {
	// set root directory	
	char temp[64];
	strcpy(temp, current_dir.c_str());
	dentry* Root = new dentry(temp, NULL); // root directory	
	current_directory = Root;
	superblock.s_root = Root;
	// set valid instructions
	string instructions[8];
	instructions[0] = "ls";
	instructions[1] = "cd";
	instructions[2] = "mkdir";
	instructions[3] = "rmdir";
	instructions[4] = "mkfile";
	instructions[5] = "rmfile";
	instructions[6] = "inode";
	instructions[7] = "exit";

	// start
	string user_inputs;
	string user_input;
	vector<string> inputs;	

	while (!Terminate) {
		inputs.clear();
		cout << student_id << ":" + current_dir + "$ ";
		getline(cin, user_inputs);
		stringstream ss(user_inputs);
		while (getline(ss, user_input, ' ')) {
			inputs.push_back(user_input);
		}
		/* enter */
		if (inputs.empty())
			continue;
		
		/* ls */
		if (inputs.front() == instructions[0]) {
			if (inputs.size() > 1) // invalid input
				continue;
			else
				Ls();
		}
		
		/* cd */
		else if (inputs.front() == instructions[1]) {
			if (inputs.size() != 2) {// invalid input				
				continue;
			}
			recovery_counter = 0;
			while (!recovery_name.empty()) recovery_name.pop();
			Cd(inputs[1]);
		}
		
		/* mkdir */
		else if (inputs.front() == instructions[2]) {
			if (inputs.size() < 2)
				continue;
			vector<string> parameter;
			for (int i = 1; i < inputs.size(); i++) {
				parameter.push_back(inputs[i]);
			}
			Mkdir(parameter);
		}
		
		/* rmdir */
		else if (inputs.front() == instructions[3]) {
			if (inputs.size() < 2)
				continue;
			vector<string> parameter;
			for (int i = 1; i < inputs.size(); i++) {
				parameter.push_back(inputs[i]);
			}
			Rmdir(parameter, current_directory);
			cout << "Now you have ..." << endl;
			cout << 973 - total_used_block << " / 973 (blocks)" << endl;
		}
		
		/* mkfile */
		else if (inputs.front() == instructions[4]) {
			if (inputs.size() != 3)
				continue;
			if (inputs[2].substr(0, 1) >= "A")
				continue;
			Mkfile(inputs[1], stoi(inputs[2]));			
		}
		
		/* rmfile */
		else if (inputs.front() == instructions[5]) {
			if (inputs.size() < 2)
				continue;
			vector<string> parameter;
			for (int i = 1; i < inputs.size(); i++) {
				parameter.push_back(inputs[i]);
			}
			Rmfile(parameter, current_directory);
			cout << "Now you have ..." << endl;
			cout << 973 - total_used_block << " / 973 (blocks)" << endl;

		}
		
		/* inode */
		else if (inputs.front() == instructions[6]) {
			if (inputs.size() != 2)
				continue;
			Inode(inputs[1]);
		}
		
		/* exit */
		else if (inputs.front() == instructions[7]) {
			break;
		}
		
		/* invalid instruction */
		else {
			continue;
		}
	}
}

void Ls() { // file and directory information of current directory
	for (int i = 0; i < current_directory->d_dentry.size(); i++) {
		cout << (current_directory->d_dentry[i])->name << " ";
	}
	for (int j = 0; j < 128; j++) {
		if (inode_table[j] == NULL) continue;
		for (int i = 0; i < current_directory->d_inode.size(); i++) {			
			if (current_directory->d_inode[i]->name == inode_table[j]->name) {
				cout << (current_directory->d_inode[i])->name << " ";
				break;
			}
		}
	}
	cout << endl;
}

void Cd(string input) { // change directory
	string location;
	vector<string> locations;
	// case ..
	if (input.substr(0,2) == "..") {
		if (current_dir == "/") {
			cout << "error" << endl;
			return;
		}
		if (input.size() > 2) {
			if (input.substr(2, 1) != "/") {
				cout << "error" << endl;
				return;
			}
			if (input.size() == 3) {
				cout << "error" << endl;
				return;
			}
		}
		// move to parent directory
		string name_save = current_directory->name; // save current directory name
		current_directory = (current_directory->parent);
		dentry* temp = current_directory;
		current_dir = "";
		vector<string> locs;
		while (temp->parent != NULL) {
			locs.push_back(temp->name);			
			temp = temp->parent;			
		}
		if (locs.size() == 0)
			current_dir = "/";
		else {
			while (!locs.empty()) {				
				current_dir = current_dir + "/" + locs.back();
				locs.pop_back();
			}
		}
		if (input.size() > 3) {
			// case for ../../
			recovery_name.push(name_save); // save name for recovery occurs
			recovery_counter++; // increment counter for representing recovery occurs
			Cd(input.substr(3));
		}
		return;
	}
	// case root
	else if (input == "/") {
		current_directory = (superblock.s_root);
		current_dir = "/";
		return;
	}
	else {
		// make path, parse by '/'		
		stringstream ts(input);
		while (getline(ts, location, '/')) {
			locations.push_back(location);
		}
	}
	// relative path
	if (input.substr(0, 1) != "/") {
		dentry* temp = current_directory;
		string temp_dir = current_dir;
		for (int i = 0; i < locations.size(); i++) {
			if (locations[i] == ".") continue;
			for (int j = 0; j < temp->d_dentry.size(); j++) {
				if (temp->d_dentry[j]->name == locations[i]) {
					temp = (temp->d_dentry[j]);
					if (temp_dir == "/")
						temp_dir = temp_dir + locations[i];
					else
						temp_dir = temp_dir + "/" + locations[i];
					break;
				}
				if((j == temp->d_dentry.size()-1) && (temp->d_dentry[j]->name != locations[i])){
					if (recovery_counter > 0)
						recovery();
					cout << "error" << endl;
					return;
				}
			}
		}
		current_directory = temp;
		current_dir = temp_dir;
	}
	// absolute path
	else if (input.substr(0, 1) == "/") {
		dentry* temp = (superblock.s_root);
		string temp_dir = "/";
		for (int i = 1; i < locations.size(); i++) {
			for (int j = 0; j < temp->d_dentry.size(); j++) {				
				if (temp->d_dentry[j]->name == locations[i]) {
					temp = (temp->d_dentry[j]);
					if (temp_dir == "/")
						temp_dir = temp_dir + locations[i];
					else
						temp_dir = temp_dir + "/" + locations[i];
					break;
				}
				if ((j == temp->d_dentry.size() - 1) && (temp->d_dentry[j]->name != locations[i])) {
					cout << "error" << endl;
					return;
				}
			}
		}
		current_directory = temp;
		current_dir = temp_dir;
	}	
}

void Mkdir(vector<string> names) { // make new directory under current directory
	// check already existing directory
	for (int j = 0; j < names.size(); j++) {
		for (int i = 0; i < current_directory->d_dentry.size(); i++) {
			if (current_directory->d_dentry[i]->name == names[j]) {
				cout << "error" << endl;
				return;
			}
		}
	}	
	// make directory
	for (int j = 0; j < names.size(); j++) {
		char temp[64];
		strcpy(temp, names[j].c_str());		
		dentry* NEW = new dentry(temp, current_directory);
		current_directory->d_dentry.push_back(NEW);
	}
}

void Rmdir(vector<string> names, dentry* CD) { // remove target directory and belongs to it under current directory
	vector<string> targets; // target directory names
	for (int i = 0; i < names.size(); i++) {
		for (int j = 0; j < CD->d_dentry.size(); j++) {
			if (CD->d_dentry[j]->name == names[i])
				targets.push_back(CD->d_dentry[j]->name);
		}
	}
	if (targets.size() != names.size()) { // invalid input
		cout << "error" << endl;
		return;
	}
	// remove target directory
	for (int i = 0; i < names.size(); i++) {
		string target = targets.back();
		targets.pop_back();
		for (int j = 0; j < CD->d_dentry.size(); j++) {
			if (CD->d_dentry[j]->name == target){
				// delete child items recursively
				vector<string> temp;
				for (int k = 0; k < CD->d_dentry[j]->d_dentry.size(); k++) {
					temp.push_back((CD->d_dentry[j]->d_dentry[k])->name);
				}
				if(!(temp.empty()))
					Rmdir(temp, (CD->d_dentry[j]));

				vector<string> temp2;
				for (int k = 0; k < CD->d_dentry[j]->d_inode.size(); k++) {
					temp2.push_back((CD->d_dentry[j]->d_inode[k])->name);
				}
				if(!(temp2.empty()))
					Rmfile(temp2, (CD->d_dentry[j]));

				// delete from current directory
				vector<dentry*>::iterator iter = (CD->d_dentry).begin();
				CD->d_dentry.erase(iter + j);

				break;
			}
		}
	}	
}

void Mkfile(string name, int size) { // make new file under current directory
	// same name already exists
	for (int i = 0; i < current_directory->d_inode.size(); i++) {
		if (current_directory->d_inode[i]->name == name) {
			cout << "error" << endl;
			return;
		}
	}
	// check enough space
	int block_size;
	if (size % 1024 == 0) 
		block_size = size / 1024;
	else
		block_size = (size / 1024) + 1;
	if (block_size <= 12) {
		if (total_used_block + block_size > 973) {
			cout << "error" << endl;
			return;
		}
	}
	else if (block_size <= 268) {
		if (total_used_block + block_size + 1 > 973) {
			cout << "error" << endl;
			return;
		}
	}
	else {
		int temp = block_size - 268;
		if (temp % 256 == 0)
			temp = temp / 256;		
		else 
			temp = (temp / 256) + 1;
		if (total_used_block + block_size + temp + 2 > 973) {
			cout << "error" << endl;
			return;
		}
	}
	// make file
	char temp[64];
	strcpy(temp, name.c_str());	
	inode* NEW = new inode(temp, size);
	current_directory->d_inode.push_back(NEW);
	inode_table[NEW->id] = NEW;
	cout << "Now you have ..." << endl;
	cout << 973-total_used_block << " / 973 (blocks)" << endl;
}

void Rmfile(vector<string> names, dentry* CD) { // remove target file under current directory	
	vector<int> ids; // target file ids
	for (int i = 0; i < names.size(); i++) {
		for (int j = 0; j < CD->d_inode.size(); j++) {
			if (CD->d_inode[j]->name == names[i]) 				
				ids.push_back(CD->d_inode[j]->id);			
		}
	}
	if (ids.size() != names.size()) { // invalid input
		cout << "error" << endl;
		return;
	}
	// remove files
	for (int i = 0; i < names.size(); i++) {
		int target_id = ids.back();
		ids.pop_back();		
		for (int j = 0; j < CD->d_inode.size(); j++) {
			if (CD->d_inode[j]->id == target_id) {
				// delete from current directory
				vector<inode*>::iterator iter = (CD->d_inode).begin();
				CD->d_inode.erase(iter + j);				
				// recover memory
				total_used_block -= inode_table[target_id]->db_num;
				total_used_block -= inode_table[target_id]->sib_num;
				total_used_block -= inode_table[target_id]->dib_num;
				// delete from inode table
				inode_table[target_id] = NULL;
				deleted_ids.push(target_id);

				break;
			}				
		}
	}	
}

void Inode(string name) { // show data about target file
	for (int i = 0; i < current_directory->d_inode.size(); i++) {
		if (current_directory->d_inode[i]->name == name) {
			cout << "ID: " << current_directory->d_inode[i]->id << endl;
			cout << "Name: " << current_directory->d_inode[i]->name << endl;
			cout << "Size: " << current_directory->d_inode[i]->size << " (bytes)" << endl;
			cout << "Direct blocks: " << current_directory->d_inode[i]->db_num << endl;
			cout << "Single indirect blocks: " << current_directory->d_inode[i]->sib_num << endl;
			cout << "Double indirect blocks: " << current_directory->d_inode[i]->dib_num << endl;
			return;
		}
	}
	cout << "error" << endl; // does not exist
}

void recovery() { // recovering current directory to original location
	while (!(recovery_name.empty())) {
		for (int i = 0; i < current_directory->d_dentry.size(); i++) {
			if (current_directory->d_dentry[i]->name == recovery_name.top()) {
				current_directory = current_directory->d_dentry[i];
				break;
			}
		}
		if (current_dir == "/")
			current_dir = current_dir + recovery_name.top();
		else
			current_dir = current_dir + "/" + recovery_name.top();		
		recovery_name.pop();
	}
	recovery_counter = 0;
}