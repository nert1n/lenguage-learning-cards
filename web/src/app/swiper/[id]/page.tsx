import SliderPage from '@/page/main/SliderPage';

function SwiperPage({ params }: { params: { id: number } }) {
	return <SliderPage id={params.id} />;
}

export default SwiperPage;
