

reload :: ()
  - getPrefs()
  - first[fromServer(Prefs) -> cache(Prefs, Sched), cache(Prefs)] :: Prefs --> Sched
  - addEmptyTransformations
  - memory.update()



filter :: Entry
  - filtertransformerFromEntry()
  - memory.append()
  - transf.mergeXOr()
  - memory.update()





* [x] first[fromServer(Prefs) -> cache(Prefs, Sched), cache(Prefs)] :: Prefs --> Sched
* [x] getLogin :: () -> Login
* [x] addEmptyTransformations() :: Sched -> (Transf, Sched)
* [x] memory.update() :: (Transf, Sched) -> (Transf, Sched)
* [x] memory.append() :: (T) -> (T, Transf, Sched)
* [x] applyTransf():: (Transf, Sched) -> Sched
* [x] view.display() :: Sched -> Sched
* [x] filtertransformerFromEntry() :: Entry -> Transf::(Entry, fn()::Sched -> Sched)
* [x] transf.mergXor():: (Transf, Transf, _) -> (Transf, _)




