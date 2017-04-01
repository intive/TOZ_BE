Ansible Playbooks
----
Script to preparing package and deploy application on  environment.


preconditions
----
exist files:
```
../build/libs/toz-core.jar
../build/libs/toz-core-dev.jar
../build/libs/toz-core-prod.jar
```


test
----
```
ansible-playbook config.yml -e "env=PROD"
ansible-playbook config.yml -e "env=CORE"
ansible-playbook config.yml -e "env=DEV"
ansible-playbook config.yml  # default env=DEV

```


use case: deploy application (include config)
----

* DEV
```
ansible-playbook deploy.yml -i hosts
ansible-playbook deploy.yml -i hosts -e "env=DEV"
```

* CORE
```
ansible-playbook deploy.yml -i hosts -e "env=CORE"
```


* PROD
```
ansible-playbook deploy.yml -i hosts -e "env=PROD"
```


