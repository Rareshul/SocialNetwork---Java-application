TABEL INAINTE / DUPA (bucle inlocuite)


Nr  Clasa.metoda                        Inainte (lab 3)                           Dupa

 1  UserService.generateId             for + if (> id) id = ...                  repo.stream().map().max().orElse(0L) + 1
 2  FriendshipService.generateId       idem                                      idem
 3  UserService.remove                 for care strange in toDelete + for        findAll(predicat).forEach(repo::remove)



INTERFETE FUNCTIONALE FOLOSITE SI UNDE

  Validator<T>        - interfata proprie; lambda in Main (regula de 30
                        caractere), andThen pentru compunere
  Predicate<T>        - Repository.findAll, UserCriteria, filter()
  Supplier<T>         - RepositoryFactory (Map<RepoTypes, Supplier<...>>)


BUCLE PASTRATE

  Ui.run(): while-ul meniului.


