package dev.zontreck.otemod.entities.monsters;


public class PossumEntity
{
	/*
	public PossumEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		//TODO Auto-generated constructor stub
	}

	private AnimationFactory factory = GeckoLibUtil.createFactory(this);


	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public PossumEntity getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return (PossumEntity) ModEntityTypes.POSSUM.get().create(pLevel);
	}

	@Override
	public boolean isFood(ItemStack pStack)
	{
		return pStack.is(Items.APPLE);
	}


	public static AttributeSupplier createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, (double)0.2F).build();
	 }
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> ev)
	{
		if(ev.isMoving())
		{
			// change anim
			return PlayState.CONTINUE;
		}

		ev.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", ILoopType.EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
	}

	@Override
	public void registerGoals()
	{
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1));
	}*/
}